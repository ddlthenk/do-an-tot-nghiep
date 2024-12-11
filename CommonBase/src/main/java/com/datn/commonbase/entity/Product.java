package com.datn.commonbase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product")
public class Product implements Serializable {
    private static final long serialVersionUID = 8L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long productId;

    @Column(name = "sku_code")
    private String skuCode;

    @Column(name = "product_title")
    private String productTitle;
    @Column(name = "product_price", columnDefinition = "int default 0")
    private int productPrice;
    @Column(name = "product_description", columnDefinition = "LONGTEXT")
    @Lob
    private String productDescription;

    @Column(name = "width")
    private int width;
    @Column(name = "length")
    private int length;
    @Column(name = "height")
    private int height;
    @Column(name = "weight")
    private int weight;

    @Column(name = "category_parent_id", nullable = false)
    private long categoryParentId;

    @Column(name = "category_id", nullable = false)
    private long categoryId;

    @Column(name = "product_rating", columnDefinition = "int default 5")
    private double productRating;
    @Column(name = "product_rating_count", columnDefinition = "int default 1")
    private int productRatingCount;

    @Column(name = "product_status")
    private boolean productStatus;

    @Column(name = "product_discount_price", columnDefinition = "int default 0")
    private int productDiscountPrice;

    @Column(name = "product_created")
    private long productCreated;
    @Column(name = "product_updated")
    private long productUpdated;

    @Column(name = "product_deleted")
    private boolean productDeleted;

    @Column(name = "product_sold", columnDefinition = "int default 0")
    private int productSold;

    @Column(name = "product_total", nullable = false)
    private int productTotal;

    @Column(name = "count_option", nullable = false, columnDefinition = "int default 1")
    private int countOption;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.ALL}, mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ProductDetails> productDetailsList;

    @PrePersist
    private void prePersist() {
        if (this.productCreated == 0) {
            this.productCreated = System.currentTimeMillis();
        }
        this.productStatus = true;
        this.productDeleted = false;
        this.productRating = 5;
        this.productRatingCount = 1;
        this.countOption = 1;
        countProductTotal();
    }

    public void countProductTotal() {
        if (this.productDetailsList != null && !this.productDetailsList.isEmpty()) {
            int totalQuantity = 0;
            int minPrice = this.productDetailsList.get(0).getPrice();
            for (ProductDetails productDetails : this.productDetailsList) {
                totalQuantity += productDetails.getTotal();
                if (productDetails.getPrice() < minPrice) {
                    minPrice = productDetails.getPrice();
                }
            }
            this.productTotal = totalQuantity;
            this.productPrice = minPrice;
            this.countOption = productDetailsList.size();
        }
    }

    public void soldChange(int sold) {
        this.productSold += sold;
    }

    public void cancelSoldChange(int sold) {
        this.productSold -= sold;
        if (this.productSold < 0) {
            this.productSold = this.productTotal;
        }
    }

    public void calculateRating(double rating) {
        double totalRate = productRating * productRatingCount;
        productRatingCount++;
        productRating = (totalRate + rating) / productRatingCount;
    }

    @PreUpdate
    private void preUpdate() {
        productUpdated = System.currentTimeMillis();
    }

    public Product clone() {
        return Product.builder()
                .productId(this.productId)
                .skuCode(this.skuCode)
                .productTitle(this.productTitle)
                .productPrice(this.productPrice)
                .productDescription(this.productDescription)
                .width(this.width)
                .length(this.length)
                .height(this.height)
                .weight(this.weight)
                .categoryParentId(this.categoryParentId)
                .categoryId(this.categoryId)
                .productRating(this.productRating)
                .productRatingCount(this.productRatingCount)
                .productStatus(this.productStatus)
                .productDiscountPrice(this.productDiscountPrice)
                .productCreated(this.productCreated)
                .productUpdated(this.productUpdated)
                .productDeleted(this.productDeleted)
                .productSold(this.productSold)
                .productTotal(this.productTotal)
                .countOption(this.countOption)
                .productDetailsList(this.productDetailsList)
                .build();
    }
}
