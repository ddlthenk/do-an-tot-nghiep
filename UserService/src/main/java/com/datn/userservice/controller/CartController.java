package com.datn.userservice.controller;

import com.datn.commonbase.constant.CartStatus;
import com.datn.commonbase.constant.ImageTypes;
import com.datn.commonbase.dto.CartForm;
import com.datn.commonbase.entity.Cart;
import com.datn.commonbase.entity.Image;
import com.datn.commonbase.entity.Product;
import com.datn.commonbase.entity.ProductDetails;
import com.datn.commonbase.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CartController {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductDetailsService productDetailsService;
    @Autowired
    CartService cartService;
    @Autowired
    ImageService imageService;

    @GetMapping("/cart")
    public String getCart(Model model, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return "redirect:/login?error=true";
        }
        String userId = userDetails.getUsername();
        List<Cart> cartList = cartService.getCartList(Long.parseLong(userId), CartStatus.IN_CART.getValue());
        CartForm cartForm = new CartForm(cartList);
        List<Long> productsIds = cartList.stream().map(Cart::getProductId).collect(Collectors.toList());
        Map<Long, Product> productMap = productService.getMapProducts(productsIds);

        List<Long> detailIds = cartList.stream().map(Cart::getDetailsId).collect(Collectors.toList());
        Map<Long, ProductDetails> detailsMap = productDetailsService.getMapProducts(detailIds);
        Map<Long, Image> imageMap = imageService.getMapOneImage(productsIds, ImageTypes.PRODUCT_IMG.getValue());

        model.addAttribute("productMap", productMap);
        model.addAttribute("detailsMap", detailsMap);
        model.addAttribute("cartForm", cartForm);
        model.addAttribute("imageMap", imageMap);
        return "cart/cart";
    }

    @PostMapping("/add-cart/{productId}")
    public String addToCart(HttpServletRequest httpServletRequest, Model model, Authentication authentication
            , @ModelAttribute("quantity") int quantity
            , @RequestParam("detailsId") long detailsId
            , @PathVariable("productId") long productId
    ) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return "redirect:/login?error=true";
        }
        userId = userDetails.getUsername(); // Lấy userId từ UserDetails
        ProductDetails productDetails = productDetailsService.getProductDetails(detailsId);
        if (productDetails.getLeftQuantity() < quantity) {
            return "redirect:/error";
        }
        Cart cart = Cart.builder().paymentStatus(CartStatus.IN_CART.getValue())
                .productId(productId)
                .detailsId(detailsId)
                .quantity(quantity)
                .totalPrice(quantity * productDetails.getPrice())
                .userId(Long.parseLong(userId)).build();
        cartService.saveCart(cart);
        if (httpServletRequest.getHeader("Referer") != null) {
            return "redirect:" + httpServletRequest.getHeader("Referer");
        }
        return "redirect:/home";
    }

    @PostMapping("/buy-now/{productId}")
    public String buyNow(HttpServletRequest httpServletRequest, Model model, Authentication authentication
            , @ModelAttribute("quantity") int quantity
            , @RequestParam("detailsId") long detailsId
            , @PathVariable("productId") long productId
    ) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/login?error=true";
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userId = userDetails.getUsername(); // Lấy userId từ UserDetails
        ProductDetails productDetails = productDetailsService.getProductDetails(detailsId);
        Cart cart = Cart.builder().paymentStatus(CartStatus.IN_CART.getValue())
                .productId(productId)
                .detailsId(detailsId)
                .quantity(quantity)
                .totalPrice(quantity * productDetails.getPrice())
                .userId(Long.valueOf(userId)).build();
        Cart savedCart = cartService.saveCart(cart);
        return "redirect:/discharge-cart?cart=" + savedCart.getCartId();
    }

    @PostMapping("/update-cart")
    public String updateCarts(@ModelAttribute CartForm cartForm) {
        for (Cart cart : cartForm.getCarts()) {
            if (cart.getQuantity() == 0) {
                deleteCart(cart.getCartId());
            } else {
                Cart updateCart = cartService.getCart(cart.getCartId());
                updateCart.setQuantity(cart.getQuantity());
                Cart savedCart = cartService.saveCart(updateCart);
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteCart(@PathVariable("id") long id) {
        boolean result = cartService.deleteCart(id);
        return "redirect:/cart";
    }

    @GetMapping("/paid-cart")
    public String handleCarts(@RequestParam("cart") String cart) {
        List<Long> cartList = Arrays.stream(cart.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return "redirect:/cart";
    }
}
