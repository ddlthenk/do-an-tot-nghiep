package com.datn.commonbase.service;

import com.datn.commonbase.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ProductService {
    public Product saveProduct(Product product);

    public List<Product> getListProducts(long categoryId, int limit);

    public List<Product> getListProducts(long categoryId, int limit, boolean status);

    public Page<Product> getTopSelling(boolean status, int page, int limit);

    public Page<Product> searchProducts(boolean status, int page, int limit, String searchValue);

    public Page<Product> getTopSellingByCategory(long categoryId, boolean status, int page, int limit);

    public Page<Product> getSortedProductPageByCategory(long categoryId, String sort, long from, long to, boolean status, int page, int limit);

    public Page<Product> getNewestProductsList(boolean status, int page, int limit);

    public Page<Product> getNewestProductsListByCategory(long categoryId, boolean status, int page, int limit);

    public Page<Product> getPageProducts(long categoryId, int limit, boolean status);

    public Map<Long, Product> getMapProducts(List<Long> productIds);

    public Page<Product> getPageProducts(long categoryId, int page, int limit, boolean status);

    public Product getProduct(long productId);
}
