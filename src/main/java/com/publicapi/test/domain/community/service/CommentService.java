package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.dto.CommentRequest;
import com.publicapi.test.domain.community.entity.Comment;
import com.publicapi.test.domain.community.entity.Post;
import com.publicapi.test.domain.community.repository.CommentRepository;
import com.publicapi.test.domain.user.dto.AlarmResponse;
import com.publicapi.test.domain.user.dto.AlarmResponseMapper;
import com.publicapi.test.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AlarmResponseMapper alarmResponseMapper;

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

    public Page<AlarmResponse> findAllByUserPost(UserEntity user, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Specification<Comment> spec = searchSpecification(user.getId());
        Page<Comment> comments = commentRepository.findAll(spec, pageable);
        return comments.map(alarmResponseMapper::toDto);
    }

    private Specification<Comment> searchSpecification(Long userId) {
        return (commentRoot, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Post> postRoot = subquery.from(Post.class);
            subquery.select(postRoot.get("id"))
                    .where(criteriaBuilder.equal(postRoot.get("author").get("id"), userId));

            return criteriaBuilder.and(
                    criteriaBuilder.notEqual(commentRoot.get("user").get("id"), userId),
                    criteriaBuilder.in(commentRoot.get("post").get("id")).value(subquery)
            );
        };
    }
}
