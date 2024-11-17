package com.datn.commonbase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tbl_address")
public class Address implements Serializable {
    private static final long serialVersionUID = 2L;
    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "province_id")
    private int provinceId;

    @Column(name = "district_id")
    private int districtId;

    @Column(name = "ward_id")
    private int wardId;
    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "specific_address")
    private String specificAddress;

    @Column(name = "is_default", nullable = false, columnDefinition = "int default 0")
    private int isDefault;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    @JsonIgnore
    private User user;
}
