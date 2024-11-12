package com.devtam.commonbase.service;

import com.devtam.commonbase.entity.ProductDetails;

import java.util.List;
import java.util.Map;

public interface ProductDetailsService {
    public ProductDetails getProductDetails(long id);

    public Map<Long, ProductDetails> getMapProducts(List<Long> productIds);

    public ProductDetails saveDetails(ProductDetails details);
}
