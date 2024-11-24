package com.datn.commonbase.service.implement;

import com.datn.commonbase.entity.Category;
import com.datn.commonbase.entity.Product;
import com.datn.commonbase.repository.ProductRepository;
import com.datn.commonbase.service.CategoryService;
import com.datn.commonbase.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryService categoryService;
    private Logger _log = LogManager.getLogger(ProductServiceImpl.class);

    @Override
    public Product saveProduct(Product product) {
        try {
            if (product.getProductDetailsList() != null && !product.getProductDetailsList().isEmpty())
                product.getProductDetailsList().forEach(productDetails -> {
                    productDetails.setProduct(product);
                });
            return productRepository.save(product);
        } catch (Exception e) {
            _log.error(e);
            return null;
        }
    }

    @Override
    public List<Product> getListProducts(long categoryId, int limit) {
        List<Product> productList = new ArrayList<>();
        if (categoryId < 0) {
            Page<Product> productPage = productRepository.findAll(PageRequest.of(0, limit));
            if (productPage != null && productPage.getSize() > 0) {
                productList = productPage.getContent();
            }
        } else {
            productList = productRepository.getAllByCategoryId(categoryId, PageRequest.of(0, limit));
        }
        return productList;
    }

    @Override
    public Page<Product> getTopSelling(boolean status, int page, int limit) {
        try {
            Page<Product> productPage = productRepository.findPageSortedBySoldRatioAndCreated(status, PageRequest.of(page, limit));
            return productPage;
        } catch (Exception e) {
            _log.error(e);
            return Page.empty();
        }
    }


    @Override
    public Page<Product> searchProducts(boolean status, int page, int limit, String searchValue) {
        try {
            Page<Product> productPage = productRepository.findAllByProductStatusAndProductTitleContaining(status, searchValue, PageRequest.of(page, limit));
            return productPage;
        } catch (Exception e) {
            _log.error(e);
            return Page.empty();
        }
    }

    @Override
    public Page<Product> getTopSellingByCategory(long categoryId, boolean status, int page, int limit) {
        Category category = categoryService.getCategoryById(categoryId);
        Page<Product> producPage;
        if (category == null) {
            return Page.empty();
        }
        if (category.getParentId() == -1) {
            producPage = productRepository.findAllByCategoryParentIdSortedBySoldRatioAndCreated(categoryId, status, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        } else {
            producPage = productRepository.findAllByCategoryIdSortedBySoldRatioAndCreated(categoryId, status, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        }
        return producPage;
    }

    @Override
    public Page<Product> getSortedProductPageByCategory(long categoryId, String sort, long from, long to, boolean status, int page, int limit) {
        try {
            Page<Product> productPage = Page.empty();
            PageRequest sortedRequest = PageRequest.of(page, limit);
            if (sort != null && !sort.isEmpty()) {
                if (sort.equals("hangmoive")) {
                    sortedRequest = PageRequest.of(page, limit, Sort.by("productCreated").descending());
                }
                if (sort.equals("giatangdan")) {
                    sortedRequest = PageRequest.of(page, limit, Sort.by("productPrice").ascending());
                }
                if (sort.equals("giagiamdan")) {
                    sortedRequest = PageRequest.of(page, limit, Sort.by("productPrice").descending());
                }
            }
            if (from != -1 && to != -1) {
                productPage = productRepository.findAllByCategoryParentIdAndProductStatusAndProductPriceBetween(categoryId, status, from, to, sortedRequest);
            } else {
                productPage = productRepository.findAllByCategoryParentIdAndProductStatus(categoryId, sortedRequest, status);
            }
            return productPage;
        } catch (Exception e) {
            _log.error(e);
            return Page.empty();
        }
    }


    @Override
    public Page<Product> getNewestProductsListByCategory(long categoryId, boolean status, int page, int limit) {
        Category category = categoryService.getCategoryById(categoryId);
        Page<Product> producPage;
        if (category == null) {
            return Page.empty();
        }
        if (category.getParentId() == -1) {
            producPage = productRepository.findAllByProductStatusAndCategoryParentIdOrderByProductCreatedDesc(status, categoryId, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        } else {
            producPage = productRepository.findAllByProductStatusAndCategoryIdOrderByProductCreatedDesc(status, categoryId, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        }
        return producPage;
    }

    @Override
    public Page<Product> getNewestProductsList(boolean status, int page, int limit) {
        Page<Product> productList = productRepository.findAllByProductStatusOrderByProductCreatedDesc(status, PageRequest.of(page, limit));
        if (productList == null || productList.isEmpty()) {
            return Page.empty();
        }
        return productList;
    }

    @Override
    public List<Product> getListProducts(long categoryId, int limit, boolean status) {
        return null;
    }

    @Override
    public Page<Product> getPageProducts(long categoryId, int limit, boolean status) {
        Page<Product> productPage = Page.empty();
        if (categoryId < 0) {
            productPage = productRepository.findAllByProductStatus(PageRequest.of(0, limit), status);
        } else {
            productPage = productRepository.findAllByCategoryParentIdAndProductStatus(categoryId, PageRequest.of(0, limit), status);
        }
        return productPage;
    }

    @Override
    public Page<Product> getPageProducts(long categoryId, int page, int limit, boolean status) {
        Page<Product> productPage = Page.empty();
        if (categoryId < 0) {
            productPage = productRepository.findAllByProductStatusOrderByProductIdDesc(PageRequest.of(page, limit), status);
        } else {
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                return null;
            }
            if (category.getParentId() == -1) {
                productPage = productRepository
                        .findAllByCategoryParentIdAndProductStatusOrderByProductCreatedDesc
                                (categoryId, status, PageRequest.of(page, limit));
            } else {
                productPage = productRepository.findAllByCategoryIdAndProductStatusOrderByProductCreatedDesc(categoryId, PageRequest.of(page, limit), status);
            }
        }
        return productPage;
    }

    @Override
    public Product getProduct(long productId) {
        Product product = productRepository.getByProductId(productId);
        return product;
    }

    @Override
    public Map<Long, Product> getMapProducts(List<Long> productIds) {
        Map<Long, Product> productMap = new HashMap<>();
        for (long id : productIds) {
            Product product = getProduct(id);
            if (product != null) {
                productMap.put(id, product);
            }
        }
        if (productMap.isEmpty()) {
            return Collections.emptyMap();
        }
        return productMap;
    }
}
