package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.dto.CommentRequest;
import com.publicapi.test.domain.community.entity.Comment;
import com.publicapi.test.domain.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void save(Long userId, Long postId, CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(commentRequest.getContent());
        comment.setIsAnonymous(commentRequest.getIsAnonymous());
        comment.setCreateDate(LocalDateTime.now());
        commentRepository.save(comment);
        commentRepository.flush();
    }

    public List<Comment> findAllByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments;
    }
}
