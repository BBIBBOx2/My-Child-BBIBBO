package com.publicapi.test.domain.community.controller;

import com.publicapi.test.domain.community.dto.CommentRequest;
import com.publicapi.test.domain.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommunityCommentController {

    private final CommentService commentService;

    @PostMapping("create/{boardId}/{postId}")
    public ResponseEntity<Void> create(HttpServletRequest request,
                                       @PathVariable Long boardId,
                                       @PathVariable Long postId,
                                       @RequestBody CommentRequest commentRequest) {
        String kakaoId = (String) request.getSession()
                                         .getAttribute("kakaoId");
        commentService.save(kakaoId, postId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create(String.format("/community/%d/%d", boardId, postId)))
                             .build();
    }
}
