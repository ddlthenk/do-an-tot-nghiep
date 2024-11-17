package com.datn.commonbase.util;

import com.datn.commonbase.entity.Cart;
import com.datn.commonbase.entity.Product;

import java.util.List;
import java.util.Map;

public class ShippingUtils {
    public static long calculateTotalWeight(List<Product> productList) {
        long totalWeight = 0;
        for (Product product : productList) {
            totalWeight += product.getWeight();
        }
        return totalWeight;
    }

    public static long calculateTotalWeight(List<Cart> cartList, Map<Long, Product> productMap) {
        long totalWeight = 0;
        for (Cart cart : cartList) {
            Product product = productMap.get(cart.getProductId());
            totalWeight += product.getWeight();
        }
        return totalWeight;
    }
}
