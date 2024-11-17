package com.datn.commonbase.entity;

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
@Table(name = "tbl_user")
public class User implements Serializable {
    private static final long serialVersionUID = 10L;
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private int gender;

    @Column(name = "language", columnDefinition = "varchar(255) default 'vi'")
    private String language;

    @Column(name = "cash", columnDefinition = "int default 0")
    private Long cash;

    @Column(name = "point", columnDefinition = "int default 0")
    private Long point;

    @PrePersist
    private void prePersist() {
        if (language == null) {
            language = "vi";
        }
    }
}
