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
@Table(name = "tbl_voucher")
public class Voucher implements Serializable {
    private static final long serialVersionUID = 11L;
    @Id
    @Column(name = "voucher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voucherId;

    @Column(name = "voucher_name")
    private String voucherName;
    @Column(name = "voucher_des")
    private String voucherDescription;
    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean status;
    @Column(name = "type")
    private int type;
    @Column(name = "start")
    private Date start;
    @Column(name = "end")
    private Date end;
    @Column(name = "min_price")
    private int minPrice;
    @Column(name = "discount")
    private int discount;
    @Column(name = "min_discount")
    private int minDiscount;
    @Column(name = "max_discount")
    private int maxDiscount;

    @Column(name = "discount_percent")
    private int discountPercent;
    @Column(name = "user_id")
    private long userId;

    @PrePersist
    private void prePersist() {
        status = true;
    }

}
