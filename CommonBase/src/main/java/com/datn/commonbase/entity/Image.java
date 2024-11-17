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
@Table(name = "tbl_image")
public class Image implements Serializable {
    private static final long serialVersionUID = 5L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long imageId;

    @Column(name = "image_type")
    private int imageType;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private boolean status;

    @Column(name = "reference_id")
    private long referenceId;

    @PrePersist
    private void prePersist() {
        if (!status) {
            status = true;
        }
    }
}
