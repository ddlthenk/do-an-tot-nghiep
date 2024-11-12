package com.devtam.commonbase.service.implement;

import com.devtam.commonbase.constant.CartStatus;
import com.devtam.commonbase.entity.Cart;
import com.devtam.commonbase.repository.CartRepository;
import com.devtam.commonbase.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Override
    public List<Cart> getCartList(long userId) {
        List<Cart> cartList = cartRepository.findAllByUserId(userId);
        if (cartList == null || cartList.isEmpty()) {
            return Collections.emptyList();
        }
        return cartList;
    }

    @Override
    public List<Cart> getCartListByOrder(long orderId) {
        List<Cart> cartList = cartRepository.findAllByOrderId(orderId);
        if (cartList == null || cartList.isEmpty()) {
            return Collections.emptyList();
        }
        return cartList;
    }

    @Override
    public List<Cart> getCartList(long userId, int cartStatus) {
        List<Cart> cartList = cartRepository.findAllByUserIdAndPaymentStatus(userId, cartStatus);
        if (cartList == null || cartList.isEmpty()) {
            return Collections.emptyList();
        }
        return cartList;
    }

    @Override
    public Cart getCart(long cartId) {
        try {
            Optional<Cart> cartOptional = cartRepository.findById(cartId);
            Cart cart = cartOptional.get();
            return cart;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public boolean deleteCart(long cartId) {
        try {
            cartRepository.deleteById(cartId);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean deactiveCart(long cartId) {
        try {
            Cart cart = cartRepository.findById(cartId).get();
            cart.setPaymentStatus(CartStatus.DElETED.getValue());
            cartRepository.save(cart);
            return true;
        } catch (Exception e) {

        }
        return false;
    }
}
