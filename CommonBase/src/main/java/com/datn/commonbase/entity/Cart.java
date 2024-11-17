package com.datn.commonbase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_cart")
public class Cart implements Serializable {
    private static final long serialVersionUID = 3L;
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @Column(name = "order_id", columnDefinition = "int default -1")
    private long orderId;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "details_id")
    private long detailsId;
    @Column(name = "product_id")
    private long productId;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private int totalPrice;
}
