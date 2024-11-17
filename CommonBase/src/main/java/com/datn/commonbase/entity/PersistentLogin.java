package com.datn.commonbase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogin implements Serializable {
    private static final long serialVersionUID = 7L;

    @Column(length = 64, nullable = false)
    private String username;

    @Id
    @Column(length = 64)
    private String series;

    @Column(length = 64, nullable = false)
    private String token;

    @Column(nullable = false)
    private Timestamp lastUsed;
}