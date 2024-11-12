package com.devtam.commonbase.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.*;

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