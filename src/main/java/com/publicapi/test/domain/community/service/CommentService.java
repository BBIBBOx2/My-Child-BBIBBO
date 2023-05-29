package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.dto.CommentRequest;
import com.publicapi.test.domain.community.entity.Comment;
import com.publicapi.test.domain.community.entity.Post;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.repository.CommentRepository;
import com.publicapi.test.domain.user.dto.AlarmResponse;
import com.publicapi.test.domain.user.dto.AlarmResponseMapper;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.repository.UserRepository;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AlarmResponseMapper alarmResponseMapper;
    private final UserRepository userRepository;

    public void save(String kakaoId, Long postId, CommentRequest commentRequest) {
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                                        .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setPostId(postId);
        comment.setContent(commentRequest.getContent());
        comment.setIsAnonymous(commentRequest.getIsAnonymous());
        comment.setCreateDate(getLocalDateTime());
        commentRepository.save(comment);
        commentRepository.flush();
    }

    private static LocalDateTime getLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneOffset zoneOffset = ZoneOffset.ofHours(9);
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);
        localDateTime = offsetDateTime.toLocalDateTime();
        return localDateTime;
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

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
