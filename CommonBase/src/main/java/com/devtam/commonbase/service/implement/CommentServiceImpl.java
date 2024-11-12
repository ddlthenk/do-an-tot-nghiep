package com.devtam.commonbase.service.implement;

import com.devtam.commonbase.entity.Comment;
import com.devtam.commonbase.repository.CommentRepository;
import com.devtam.commonbase.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;
    private final Logger _log = LogManager.getLogger(CommentServiceImpl.class);

    @Override
    public Comment getAComment(long commentId) {
        try {
            return commentRepository.findById(commentId).orElse(null);
        } catch (Exception e) {
            _log.error(e);
        }
        return null;
    }

    @Cacheable(value = "comments", key = "#productId + '-' + #page + '-' + #limit")
    @Override
    public List<Comment> getCommentsOfProduct(long productId, int page, int limit) {
        try {
            return commentRepository.findLatestCommentsByProductIdAndStatus(productId, true, PageRequest.of(page, limit));
        } catch (Exception e) {
            _log.error(e);
        }
        return null;
    }

    @Override
    @CacheEvict(value = "comments", key = "'productId:' + #comment.productId + ':*'", beforeInvocation = true)
    public Comment addComment(Comment comment) {
        try {
            return commentRepository.save(comment);
        } catch (Exception e) {
            _log.error(e);
        }
        return null;
    }

    @Override
    public boolean deleteComment(Comment comment) {
        try {
            commentRepository.delete(comment);
            return true;
        } catch (Exception e) {
            _log.error(e);
        }
        return false;
    }

    @Override
    public boolean deleteComment(long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null) {
                return false;
            }
            comment.setStatus(false);
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            _log.error(e);
        }
        return false;
    }
}
