package com.datn.userservice.controller;

import com.datn.commonbase.entity.Cart;
import com.datn.commonbase.entity.Comment;
import com.datn.commonbase.entity.Order;
import com.datn.commonbase.entity.Product;
import com.datn.commonbase.service.CartService;
import com.datn.commonbase.service.CommentService;
import com.datn.commonbase.service.OrderService;
import com.datn.commonbase.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;

    @PostMapping("/add-comment/{orderId}")
    public String addComment(HttpServletRequest request, Authentication authentication,
                             @PathVariable("orderId") long orderId,
                             @ModelAttribute("summary") String summary,
                             @RequestParam(value = "star", defaultValue = "5") int rating,
                             @ModelAttribute("comment") String comments) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return "redirect:/login?error=true";
        }
        userId = userDetails.getUsername();
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.isCommented()) {
            return "redirect:/error";
        }
        Map<Long, String> productMap = new HashMap<>();
        List<Cart> cartList = cartService.getCartListByOrder(orderId);
        for (Cart cart : cartList) {
            if (!productMap.containsKey(cart.getProductId())) {
                Comment comment = Comment.builder().summary(summary).rating(rating).comments(comments)
                        .userId(Long.parseLong(userId)).productId(cart.getProductId()).detailsId(cart.getDetailsId()).build();
                commentService.addComment(comment);
                Product product = productService.getProduct(cart.getProductId());
                product.calculateRating(rating);
                productService.saveProduct(product);
                productMap.put(cart.getProductId(), "true");
            }
        }
        order.setCommented(true);
        orderService.saveOrder(order);
        String returnUrl = request.getHeader("Referer");
        if (returnUrl == null) {
            returnUrl = "/home";
        }
        return "redirect:" + returnUrl;
    }

}
