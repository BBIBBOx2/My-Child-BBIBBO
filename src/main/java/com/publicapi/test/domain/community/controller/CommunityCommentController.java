package com.publicapi.test.domain.community.controller;

import com.publicapi.test.domain.community.dto.CommentRequest;
import com.publicapi.test.domain.community.service.CommentService;
import com.publicapi.test.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommunityCommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("{boardId}/{postId}/create")
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

    @GetMapping("{commentId}/delete")
    public ResponseEntity<String> delete(HttpServletRequest request,
                                         @PathVariable Long commentId) {
        Long userId = getUserId(request);
        Long authorId = commentService.findById(commentId)
                                      .getUser()
                                      .getId();

        if (userId.equals(authorId)) {
            commentService.delete(commentId);
            return ResponseEntity.ok()
                                 .build();
        }

        return ResponseEntity.badRequest()
                             .body("삭제할 권한이 없습니다.");
    }

    private Long getUserId(HttpServletRequest request) {
        String kakaoId = (String) request.getSession()
                                         .getAttribute("kakaoId");
        Long userId = userService.getLoginUser(kakaoId)
                                 .getId();
        return userId;
    }
}
