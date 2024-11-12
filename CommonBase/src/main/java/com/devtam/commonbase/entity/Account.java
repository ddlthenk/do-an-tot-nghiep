package com.devtam.commonbase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tbl_account")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "phoneNumber", nullable = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(255) default '$2a$10$aj7UEF.5O2Cbjid5M.1ezeS2uQoSnvMG72CP8iuOYsjEcDm72fhiW'")
    private String password;

    @Column(name = "role", nullable = false, columnDefinition = "int default 1")
    private int role;

    @Column(name = "enable", nullable = false, columnDefinition = "boolean default true")
    private boolean enable;

    @Column(name = "locked", nullable = false, columnDefinition = "boolean default false")
    private boolean locked;

    @PrePersist
    private void prePersist() {
        if (!enable) {
            enable = true;
        }
        if (this.userName == null) {
            this.userName = "GUEST" + this.accountId;
        }
    }

    @PostPersist
    public void postPersist() {

    }
}
