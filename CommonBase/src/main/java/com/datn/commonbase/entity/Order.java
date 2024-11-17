package com.datn.commonbase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 6L;
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_status")
    private int orderStatus;
    @Column(name = "commented")
    private boolean commented;
    @Column(name = "sub_total")
    private int subTotal;
    @Column(name = "delivery_charges")
    private int deliveryCharges;
    @Column(name = "total")
    private int total;
    @Column(name = "payment_method")
    private int paymentMethod;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "order_infor", columnDefinition = "LONGTEXT")
    private String orderInfor;

    @Column(name = "user_id")
    private long userId;
    @Column(name = "voucher_id")
    private long voucherId;

    /* address*/
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_phone")
    private String customerPhone;
    @Column(name = "address_id")
    private Long addressId;
    @Column(name = "province_id")
    private int provinceId;
    @Column(name = "province_name")
    private String provinceName;
    @Column(name = "district_id")
    private int districtId;
    @Column(name = "district_name")
    private String districtName;
    @Column(name = "ward_id")
    private int wardId;
    @Column(name = "ward_name")
    private String wardName;
    @Column(name = "specific_address")
    private String specificAddress;

    @PrePersist
    private void prePersist() {
        orderDate = new Date(System.currentTimeMillis());
        commented = false;
    }
}
