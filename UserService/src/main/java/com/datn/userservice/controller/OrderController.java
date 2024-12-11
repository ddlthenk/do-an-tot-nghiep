package com.datn.userservice.controller;

import com.datn.commonbase.constant.CartStatus;
import com.datn.commonbase.constant.PaymentMethod;
import com.datn.commonbase.constant.PaymentStatus;
import com.datn.commonbase.dto.CartForm;
import com.datn.commonbase.entity.*;
import com.datn.commonbase.service.*;
import com.datn.commonbase.util.ShippingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductDetailsService productDetailsService;
    @Autowired
    VoucherService voucherService;
    @Autowired
    OrderService orderService;
    @Autowired
    HttpSession session;
    @Autowired
    VNPayService payService;
    @Autowired
    GHNService ghnService;
    @Autowired
    CommentService commentService;
    private final Logger _log = LogManager.getLogger(OrderController.class);

    @GetMapping("/order-list")
    public String orderList(Model model, Authentication authentication
            , @RequestParam(value = "page", defaultValue = "0") int page
            , @RequestParam(value = "limit", defaultValue = "10") int limit) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/login?error=true";
        }
        if (page > 0) {
            page--;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userId = userDetails.getUsername();
        Page<Order> orderPage = orderService.getOrderPageByUser(Long.parseLong(userId), page, limit);
        if (!orderPage.getContent().isEmpty()) {
            orderPage.getContent().stream().forEach(order -> {
                if (order.getOrderStatus() != PaymentStatus.DONE.getValue() && order.getOrderStatus() != PaymentStatus.CANCELED.getValue()
                        && order.getOrderStatus() != PaymentStatus.SHIPPED.getValue()) {

                }
            });
        }
        model.addAttribute("orderPage", orderPage);
//        List<Long> productIds = orderPage.getContent().stream().map(Product::getProductId).collect(Collectors.toList());
//        Map<Long, Product> productMap = productService.getMapProducts();
        return "order/order-list";
    }

    @GetMapping("/handle-status/{orderId}")
    public String handleOrderStatus(Model model, Authentication authentication, @PathVariable("orderId") long orderId
            , @RequestParam(value = "to", defaultValue = "-1") int to
    ) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return "redirect:/login?error=true";
        }
        userId = userDetails.getUsername();
        if (to == -1) {
            return "redirect:/error";
        }
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/error";
        }
        if (order.getUserId() != Long.parseLong(userId)) {
            return "redirect:/error";
        }
        if (to == PaymentStatus.DONE.getValue()) {
            if (order.getOrderStatus() == PaymentStatus.SHIPPED.getValue()) {
                order.setOrderStatus(PaymentStatus.DONE.getValue());
                orderService.saveOrder(order);
            }
        }
        if (to == PaymentStatus.CANCELED.getValue()) {
            Map<String, Object> orderDetails = ghnService.getOrderInfo(order.getOrderCode());
            if ((orderDetails != null && !orderDetails.isEmpty() && orderDetails.get("status") != null)
                    && (orderDetails.get("status").equals("picking") || orderDetails.get("status").equals("ready_to_pick"))) {
                if (order.getOrderStatus() == PaymentStatus.APPROVED.getValue() || order.getOrderStatus() == PaymentStatus.WAITING.getValue()) {
                    String cancelResponse = ghnService.cancelOrder(order.getOrderCode());
                    if (cancelResponse.equals("200")) {
                        order.setOrderStatus(PaymentStatus.CANCELED.getValue());
                        orderService.saveOrder(order);
                        List<Cart> cartList = cartService.getCartListByOrder(order.getOrderId());
                        for (Cart cart : cartList) {
                            /* update product quantity */
                            Product updateProduct = productService.getProduct(cart.getProductId());
                            updateProduct.cancelSoldChange(cart.getQuantity());
                            productService.saveProduct(updateProduct);

                            /* update ProductDetails quantity */
                            ProductDetails updateProductDetails = productDetailsService.getProductDetails(cart.getDetailsId());
                            updateProductDetails.cancelSoldChange(cart.getQuantity());
                            productDetailsService.saveDetails(updateProductDetails);
                        }
                    }
                }
            }
        }
        return "redirect:/order-details/" + orderId;
    }

    @GetMapping("/order-details/{orderId}")
    public String orderDetails(Model model, Authentication authentication, @PathVariable("orderId") long orderId
            , @RequestParam(value = "cancel-error", defaultValue = "") String cancelError
    ) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/login";
        }
        if (cancelError.equals("true")) {
            model.addAttribute("cancelError", "true");
        }
        Order order = orderService.getOrderById(orderId);
        List<Cart> cartList = cartService.getCartListByOrder(orderId);
        List<Long> productIds = cartList.stream().map(Cart::getProductId).collect(Collectors.toList());
        Map<Long, Product> productMap = productService.getMapProducts(productIds);
        model.addAttribute("order", order);
        model.addAttribute("productMap", productMap);
        model.addAttribute("cartList", cartList);
        return "order/order-details";
    }

    @GetMapping("/discharge-cart")
    public String dischargeCart(Model model, Authentication authentication, @RequestParam("cart") String cart) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/login?error=true";
        }
        List<Long> cartListIds = Arrays.stream(cart.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        if (cartListIds.size() == 0) {
            return "redirect:/cart";
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userId = userDetails.getUsername();

        List<Cart> cartList = new ArrayList<>();
        for (long i : cartListIds) {
            Cart cartItem = cartService.getCart(i);
            if (cartItem.getUserId() != Long.parseLong(userId)) {
                return "redirect:/cart";
            }
            cartList.add(cartItem);
        }
        List<Long> productsIds = cartList.stream().map(Cart::getProductId).collect(Collectors.toList());
        Map<Long, Product> productMap = productService.getMapProducts(productsIds);

        List<Long> detailIds = cartList.stream().map(Cart::getDetailsId).collect(Collectors.toList());
        Map<Long, ProductDetails> detailsMap = productDetailsService.getMapProducts(detailIds);
        List<Voucher> voucherList = voucherService.getListVoucherByUserIdAndAllowed(Long.valueOf(userId), true, 0, 10);
        List<Voucher> voucherList2 = voucherService.getListVoucherByUserIdAndAllowed(Long.valueOf(-1), true, 0, 10);
        List<Voucher> voucherList3 = voucherService.getListVoucherByUserIdAndAllowed(Long.valueOf(0), true, 0, 10);
        voucherList.addAll(voucherList2);
        voucherList.addAll(voucherList3);
        CartForm cartForm = new CartForm(cartList);
        long totalWeight = ShippingUtils.calculateTotalWeight(cartList, productMap);

        model.addAttribute("totalWeight", totalWeight);
        model.addAttribute("cartForm", cartForm);
        model.addAttribute("voucherList", voucherList);
        model.addAttribute("productMap", productMap);
        model.addAttribute("detailsMap", detailsMap);
        return "order/discharge-cart";
    }

    @PostMapping("/discharge-cart")
    public String checkout(Model model, Authentication authentication
            , HttpServletRequest httpServletRequest
            , @ModelAttribute CartForm cartForm
            , @ModelAttribute("customer-name") String name
            , @ModelAttribute("phone-number") String phoneNumber
            , @ModelAttribute("provinceSelected") int provinceSelected
            , @ModelAttribute("provinceSelectedName") String provinceSelectedName
            , @ModelAttribute("districtSelected") int districtSelected
            , @ModelAttribute("districtSelectedName") String districtSelectedName
            , @ModelAttribute("wardSelected") int wardSelected
            , @ModelAttribute("wardSelectedName") String wardSelectedName
            , @ModelAttribute("address") String address
            , @RequestParam(value = "voucherSelected", defaultValue = "-1") int voucherSelected
            , @RequestParam(value = "selectedMethod", defaultValue = "1") int selectedMethod
            , @RequestParam(value = "sub-total", defaultValue = "0") int subTotal
            , @RequestParam(value = "shipping", defaultValue = "0") int shipping
            , @RequestParam(value = "total", defaultValue = "0") int total
    ) {
        try {
            String userId = "-1";
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return "redirect:/login?required=true";
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userId = userDetails.getUsername();
            Order newOrder = Order.builder().orderStatus(PaymentStatus.WAITING.getValue())
                    .customerName(name)
                    .customerPhone(phoneNumber)
                    .provinceId(provinceSelected)
                    .provinceName(provinceSelectedName)
                    .districtId(districtSelected)
                    .districtName(districtSelectedName)
                    .wardId(wardSelected)
                    .wardName(wardSelectedName)
                    .specificAddress(address)
                    .userId(Long.valueOf(userId))
                    .subTotal(subTotal)
                    .deliveryCharges(shipping)
                    .total(total)
                    .voucherId(voucherSelected)
                    .build();
            Order savedOrder = orderService.saveOrder(newOrder);
            /* update order */
            for (Cart cart : cartForm.getCarts()) {
                Cart updateCart = cartService.getCart(cart.getCartId());
                if (updateCart != null) {
                    updateCart.setOrderId(savedOrder.getOrderId());
                    updateCart.setPaymentStatus(CartStatus.IN_ORDER.getValue());
                }
                cartService.saveCart(updateCart);
                /* update product quantity */
                Product updateProduct = productService.getProduct(updateCart.getProductId());
                updateProduct.soldChange(updateCart.getQuantity());
                productService.saveProduct(updateProduct);

                /* update ProductDetails quantity */
                ProductDetails updateProductDetails = productDetailsService.getProductDetails(updateCart.getDetailsId());
                updateProductDetails.soldChange(updateCart.getQuantity());
                productDetailsService.saveDetails(updateProductDetails);
            }
            if (voucherSelected != -1) {
                Voucher voucher = voucherService.getVoucher(voucherSelected);
                voucher.setStatus(false);
                voucherService.saveVoucher(voucher);
            }
            switch (selectedMethod) {
                case 2:
                    break;
                case 3:
                    String baseUrl = httpServletRequest.getScheme() + "://"
                            + httpServletRequest.getServerName() + ":"
                            + httpServletRequest.getServerPort() + "/order-success/" + savedOrder.getOrderId();
                    String redirectHref = payService.createOrder(savedOrder.getTotal(), baseUrl);
                    savedOrder.setPaymentMethod(PaymentMethod.VNPAY.getValue());
                    savedOrder.setOrderInfor(redirectHref);
                    orderService.saveOrder(savedOrder);
                    return "redirect:" + redirectHref;
                default:
                    newOrder.setPaymentMethod(PaymentMethod.COD.getValue());
                    boolean shipResult = ghnService.sendPostRequest2(2, newOrder);
                    if (shipResult) {
                        newOrder.setOrderStatus(PaymentStatus.APPROVED.getValue());
                    }
                    orderService.saveOrder(savedOrder);
                    return "redirect:/order-success/" + savedOrder.getOrderId() + "?cod=true";
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return "redirect:/cart?error=true";
    }

    @GetMapping("/order-success/{orderId}")
    public String orderSuccess(@PathVariable("orderId") long orderId, HttpServletRequest request, Model model) {
        model.addAttribute("orderId", orderId);
//        if (request.getParameter("vnp_SecureHash") != null && !request.getParameter("vnp_SecureHash").equals("")) {
//            int orderResult = payService.orderReturn(request);
//            if (orderResult == 1) {
//                Order order = orderService.getOrderById(orderId);
//                if (payService.queryDr(order.getOrderInfor()).equals("00")) {
//                    if (order.getOrderStatus() != PaymentStatus.PAID.getValue()) {
//                        order.setOrderStatus(PaymentStatus.PAID.getValue());
//                    }
//                    orderService.saveOrder(order);
//                    ghnService.sendPostRequest2(1, order);
//                } else {
//                    model.addAttribute("paid", "false");
//                }
//
//            }
//        }
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/error";
        }
        if (order.getPaymentMethod() == PaymentMethod.COD.getValue()) {
            model.addAttribute("cod", "true");
            model.addAttribute("paid", "true");
        } else {
            String responseCode = request.getParameter("vnp_ResponseCode");
            if (responseCode != null && responseCode.equals("00")) {
                order.setOrderStatus(PaymentStatus.PAID.getValue());
                model.addAttribute("paid", "true");
            } else {
                order.setOrderStatus(PaymentStatus.CANCELED.getValue());
                model.addAttribute("paid", "false");
            }
            orderService.saveOrder(order);
        }
        return "order/order-success";
    }
}
