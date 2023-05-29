package com.publicapi.test.domain.community.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
public class PostRequest {

    private Long userId = 1L;

    @NotEmpty
    private Long boardId;

    @NotEmpty
    private Long regionId;

    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String title;


    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;

    private List<String> hashtags;

    private Boolean isAnonymous;

    @Override
    public String toString() {
        return "PostRequest{" +
                "userId=" + userId +
                ", boardId=" + boardId +
                ", regionId=" + regionId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hashtags=" + hashtags +
                ", isAnonymous=" + isAnonymous +
                '}';
    }
}
