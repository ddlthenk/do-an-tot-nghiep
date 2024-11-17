package com.datn.commonbase.service;

import com.datn.commonbase.entity.Comment;

import java.util.List;

public interface CommentService {
    public Comment getAComment(long commentId);

    public List<Comment> getCommentsOfProduct(long productId, int page, int limit);

    public Comment addComment(Comment comment);

    public boolean deleteComment(Comment comment);

    public boolean deleteComment(long commentId);
}
