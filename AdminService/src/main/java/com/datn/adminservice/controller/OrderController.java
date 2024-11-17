package com.datn.adminservice.controller;

import com.datn.commonbase.constant.PaymentStatus;
import com.datn.commonbase.entity.Cart;
import com.datn.commonbase.entity.Order;
import com.datn.commonbase.entity.Product;
import com.datn.commonbase.entity.ProductDetails;
import com.datn.commonbase.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductDetailsService productDetailsService;
    private static GHNService ghnService = new GHNService();
    private final Logger _log = LogManager.getLogger(OrderController.class);

    @GetMapping("/order-list")
    public String getListOrder(Model model, @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        if (page > 0) {
            page--;
        }
        Page<Order> orderPage = orderService.getOrderPage(page, 10);
        model.addAttribute("orderPage", orderPage);
        return "order/order-list";
    }

    @PostMapping("/orders/search")
    public String searchProducts(Model model, @RequestParam(required = false, value = "page", defaultValue = "0") int page
            , @RequestParam("searchValue") String searchValue) {
        try {
            if (page > 0) {
                page--;
            }
            Page<Order> orderPage = orderService.searchOrderPage(searchValue, page, 10);
            model.addAttribute("orderPage", orderPage);
            return "order/order-list";
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "redirect:/admin/error";
        }
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

        Map<String, Object> infoResult = ghnService.getOrderInfo(order.getOrderCode());
        if (infoResult.get("status") != null && infoResult.get("status").equals("delivered")) {
            if (order.getOrderStatus() != PaymentStatus.SHIPPED.getValue()) {
                order.setOrderStatus(PaymentStatus.SHIPPED.getValue());
                orderService.saveOrder(order);
            }
        }
        if (infoResult.get("status") != null && (infoResult.get("status").equals("cancel") || infoResult.get("status").equals("null"))) {
            if (order.getOrderStatus() != PaymentStatus.CANCELED.getValue()) {
                order.setOrderStatus(PaymentStatus.CANCELED.getValue());
                orderService.saveOrder(order);
            }
        }

        model.addAttribute("order", order);
        model.addAttribute("productMap", productMap);
        model.addAttribute("cartList", cartList);
        return "order/order-details";
    }

    @GetMapping("/handle-status/{orderId}")
    public String handleOrderStatus(Model model, Authentication authentication, @PathVariable("orderId") long orderId
            , @RequestParam(value = "to", defaultValue = "-1") int to
    ) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
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
        return "redirect:/admin/order-details/" + orderId;
    }
}
