package com.publicapi.test.domain.community.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class CommentRequest {

    @NotEmpty(message = "댓글을 작성해주세요.")
    private String content;

    private Boolean isAnonymous;
}
