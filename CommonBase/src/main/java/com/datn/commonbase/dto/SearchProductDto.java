package com.datn.commonbase.dto;

import com.datn.commonbase.entity.Product;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductDto {
    private long productId;
    private String skuCode;
    private String productTitle;
    private int productPrice;
    private String productDescription;
    private int width;
    private int length;
    private int height;
    private int weight;
    private long categoryParentId;
    private long categoryId;
    private double productRating;
    private int productRatingCount;
    private boolean productStatus;
    private int productDiscountPrice;
    private long productCreated;
    private long productUpdated;
    private boolean productDeleted;
    private int productSold;
    private int productTotal;
    private int countOption;
    private String imageUrl;

    public void setProduct(Product product) {
        this.productId = product.getProductId();
        this.skuCode = product.getSkuCode();
        this.productTitle = product.getProductTitle();
        this.productPrice = product.getProductPrice();
        this.productDescription = product.getProductDescription();
        this.width = product.getWidth();
        this.length = product.getLength();
        this.height = product.getHeight();
        this.weight = product.getWeight();
        this.categoryParentId = product.getCategoryParentId();
        this.categoryId = product.getCategoryId();
        this.productRating = product.getProductRating();
        this.productRatingCount = product.getProductRatingCount();
        this.productStatus = product.isProductStatus();
        this.productDiscountPrice = product.getProductDiscountPrice();
        this.productCreated = product.getProductCreated();
        this.productUpdated = product.getProductUpdated();
        this.productDeleted = product.isProductDeleted();
        this.productSold = product.getProductSold();
        this.productTotal = product.getProductTotal();
        this.countOption = product.getCountOption();
    }


    public static SearchProductDto build(Product product, String imageUrl) {
        return SearchProductDto.builder()
                .productId(product.getProductId())
                .skuCode(product.getSkuCode())
                .productTitle(product.getProductTitle())
                .productPrice(product.getProductPrice())
                .productDescription(product.getProductDescription())
                .width(product.getWidth())
                .length(product.getLength())
                .height(product.getHeight())
                .weight(product.getWeight())
                .categoryParentId(product.getCategoryParentId())
                .categoryId(product.getCategoryId())
                .productRating(product.getProductRating())
                .productRatingCount(product.getProductRatingCount())
                .productStatus(product.isProductStatus())
                .productDiscountPrice(product.getProductDiscountPrice())
                .productCreated(product.getProductCreated())
                .productUpdated(product.getProductUpdated())
                .productDeleted(product.isProductDeleted())
                .productSold(product.getProductSold())
                .productTotal(product.getProductTotal())
                .countOption(product.getCountOption())
                .imageUrl(imageUrl)
                .build();
    }
}
