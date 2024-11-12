package com.devtam.commonbase.service;

import com.devtam.commonbase.entity.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getCartList(long userId);

    List<Cart> getCartListByOrder(long orderId);

    List<Cart> getCartList(long userId, int cartStatus);

    public Cart getCart(long cartId);

    Cart saveCart(Cart cart);

    public boolean deleteCart(long cartId);

    public boolean deactiveCart(long cartId);
}
