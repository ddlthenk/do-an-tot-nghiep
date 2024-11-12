package com.devtam.commonbase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_comments")
public class Comment implements Serializable {
    private static final long serialVersionUID = 12L;
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "summary")
    private String summary;

    @Column(name = "comments", columnDefinition = "LONGTEXT")
    private String comments;

    @Column(name = "date")
    private long date;

    @Column(name = "status")
    private boolean status;

    @Column(name = "product_id")
    private long productId;
    @Column(name = "details_id")
    private long detailsId;
    @Column(name = "user_id")
    private long userId;

    @PrePersist
    private void prePersist() {
        status = true;
        date = System.currentTimeMillis();
    }
}
