package com.datn.commonbase.service.implement;

import com.datn.commonbase.entity.ProductDetails;
import com.datn.commonbase.repository.ProductDetailsRepository;
import com.datn.commonbase.service.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
    @Autowired
    ProductDetailsRepository productDetailsRepository;

    @Override
    public ProductDetails getProductDetails(long id) {
        Optional<ProductDetails> details = productDetailsRepository.findById(id);
        return details.orElse(null);
    }

    @Override
    public Map<Long, ProductDetails> getMapProducts(List<Long> productIds) {
        Map<Long, ProductDetails> productMap = new HashMap<>();
        for (long id : productIds) {
            ProductDetails product = getProductDetails(id);
            if (product != null) {
                productMap.put(id, product);
            }
        }
        if (productMap.isEmpty()) {
            return Collections.emptyMap();
        }
        return productMap;
    }

    @Override
    public ProductDetails saveDetails(ProductDetails details) {
        return productDetailsRepository.save(details);
    }
}
