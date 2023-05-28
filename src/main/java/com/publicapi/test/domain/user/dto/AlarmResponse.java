package com.publicapi.test.domain.user.dto;

import com.publicapi.test.domain.community.entity.Board;
import com.publicapi.test.domain.community.entity.Comment;
import com.publicapi.test.domain.community.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AlarmResponse {

    private Board board;
    private Post post;
    private Comment comment;
    private LocalDateTime createDate;

}
