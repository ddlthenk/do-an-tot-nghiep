package com.datn.commonbase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tbl_product_details")
public class ProductDetails implements Serializable {
    private static final long serialVersionUID = 9L;
    @Id
    @Column(name = "detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailId;

    @Column(name = "option_value")
    private String optionValue;

    @Column(name = "price", nullable = false)
    private int price;
    @Column(name = "left_quantity", nullable = false)
    private int leftQuantity;
    @Column(name = "total", nullable = false)
    private int total;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @JsonIgnore
    private Product product;

    @PrePersist
    private void prePersist() {
        leftQuantity = total;
    }

    public void soldChange(int sold) {
        this.leftQuantity -= sold;
    }

    public void cancelSoldChange(int sold) {
        this.leftQuantity += sold;
    }
}
