package com.devtam.commonbase.repository;

import com.devtam.commonbase.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByProductIdAndStatusEquals(long productId, boolean status, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Comment c WHERE c.productId = :productId AND c.status = :status GROUP BY c.userId")
    List<Comment> findDistinctCommentsByProductIdAndStatus(long productId, boolean status, Pageable pageable);

    @Query(value = "SELECT c.* FROM tbl_comments c INNER JOIN (SELECT user_id, MAX(comment_id) AS latest_comment_id FROM tbl_comments WHERE product_id = ?1 AND status = ?2 GROUP BY user_id) AS latest_comments ON c.user_id = latest_comments.user_id AND c.comment_id = latest_comments.latest_comment_id WHERE c.product_id = ?1 AND c.status = ?2", nativeQuery = true)
    List<Comment> findLatestCommentsByProductIdAndStatus(Long productId, boolean status, Pageable pageable);
}
