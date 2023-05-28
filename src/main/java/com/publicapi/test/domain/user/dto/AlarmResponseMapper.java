package com.publicapi.test.domain.user.dto;

import com.publicapi.test.domain.community.entity.Comment;
import com.publicapi.test.domain.community.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class AlarmResponseMapper {
    public AlarmResponse toDto(Comment comment) {
        AlarmResponse dto = new AlarmResponse();
        Post post = comment.getPost();
        dto.setPost(post);
        dto.setBoard(post.getBoard());
        dto.setComment(comment);
        dto.setCreateDate(comment.getCreateDate());
        return dto;
    }
}
