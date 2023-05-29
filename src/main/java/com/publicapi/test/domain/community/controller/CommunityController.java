package com.publicapi.test.domain.community.controller;

import com.publicapi.test.domain.community.dto.PostRequest;
import com.publicapi.test.domain.community.entity.*;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.service.*;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
import com.publicapi.test.domain.hospital.repository.RegionRepository;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.service.UserService;
import com.publicapi.test.s3.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("community")
public class CommunityController {

    private final UserService userService;
    private final PostService postService;
    private final HashtagService hashtagService;
    private final PostTagService postTagService;
    private final PostImageService postImageService;
    private final RegionRepository regionRepository;
    private final BoardService boardService;
    private final CommentService commentService;
    private final ImageUploadService imageUploadService;

    @GetMapping()
    public String mainPage() {
        return "redirect:/community/1";
    }

    @GetMapping("{boardId}")
    public String showPosts(Model model,
                            @PathVariable int boardId,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "search", defaultValue = "") String search,
                            @RequestParam(value = "region", defaultValue = "0") long regionType,
                            @RequestParam(value = "sortType", defaultValue = "recent") String sortType) {
        Page<Post> postPage = postService.findAllPosts(page, boardId, search, regionType, sortType);
        if (postPage == null) {
            throw new NotFoundException("게시물들을 찾을 수 없습니다.");
        }

        List<RegionEntity> regions = regionRepository.findAll();

        model.addAttribute("postPage", postPage);
        model.addAttribute("board", boardId);
        model.addAttribute("search", search);
        model.addAttribute("regions", regions);
        model.addAttribute("regionType", regionType);
        model.addAttribute("sortType", sortType);
        return "community/community_list";
    }

    @GetMapping("{boardId}/{postId}")
    public String showPost(Model model,
                           HttpServletRequest request,
                           @PathVariable Long boardId,
                           @PathVariable Long postId) {
        String kakaoId = (String) request.getSession()
                                         .getAttribute("kakaoId");
        Optional<UserEntity> userEntity = userService.getLoginOptionalUser(kakaoId);
        UserEntity user = null;

        Post post = postService.findPostById(postId);
        List<PostImage> postImages = postImageService.findPostImageByPostId(postId);
        List<Comment> comments = commentService.findAllByPostId(postId);
        List<PostTag> postTags = postTagService.findAllByPostId(postId);

        boolean isAlreadyScrap = false;
        if (userEntity.isPresent() && post.getScrap().contains(userEntity.get().getId())) {
            isAlreadyScrap = true;
            user = userEntity.get();
        }

        model.addAttribute("user", user);
        model.addAttribute("isAlreadyScrap", isAlreadyScrap);
        model.addAttribute("board", boardId);
        model.addAttribute("post", post);
        model.addAttribute("postImages", postImages);
        model.addAttribute("comments", comments);
        model.addAttribute("postTags", postTags);
        return "community/community_detail";
    }

    @GetMapping("{boardId}/write")
    public String loadWriteForm(Model model,
                                HttpServletRequest request,
                                @PathVariable int boardId) {
        String kakaoId = (String) request.getSession()
                                         .getAttribute("kakaoId");
        UserEntity user = userService.getLoginUser(kakaoId);
        List<Board> boards = boardService.findAll();
        List<RegionEntity> regions = regionRepository.findAll();

        model.addAttribute("userRegion", user.getRegion().getId());
        model.addAttribute("boardId", boardId);
        model.addAttribute("boards", boards);
        model.addAttribute("regions", regions);

        return "community/community_write";
    }

    @PostMapping("{boardId}/write")
    public ResponseEntity<Object> savePost(HttpServletRequest request,
                                           @RequestPart(name = "contentData") PostRequest postRequest,
                                           @RequestPart(name = "images", required = false) List<MultipartFile> images) {
        String kakaoId = (String) request.getSession()
                                         .getAttribute("kakaoId");
        Long postId = postService.create(postRequest, kakaoId);

        if (postRequest.getHashtags() != null) {
            List<Long> hashtagIds = hashtagService.create(postRequest.getHashtags());
            postTagService.create(postId, hashtagIds);
        }
        if (images != null) {
            savePostImage(postId, images);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create(String.format("/community/%d/%d", postRequest.getBoardId(), postId)))
                             .build();
    }

    private void savePostImage(Long postId, List<MultipartFile> images) {
        for (MultipartFile image : images) {
            String imageUrl = imageUploadService.uploadImage(image);
            postImageService.saveImage(postId, imageUrl);
        }
    }

    @GetMapping("{boardId}/{postId}/scrap")
    public ResponseEntity<String> scrapPost(HttpServletRequest request,
                                            @PathVariable("postId") Long postId) {
        String kakaoId = (String) request.getSession()
                                         .getAttribute("kakaoId");
        Long userId = userService.getLoginUser(kakaoId)
                                 .getId();
        Post post = postService.findPostById(postId);

        if (userId.equals(post.getAuthor().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("내가 작성한 글은 스크랩할 수 없습니다.");
        }

        postService.scrap(userId, postId);

        return ResponseEntity.ok()
                             .body("스크랩했습니다.");
    }

    @GetMapping("{boardId}/{postId}/unscrap")
    public ResponseEntity<String> unscrapPost(HttpServletRequest request,
                                            @PathVariable("postId") Long postId) {
        String kakaoId = (String) request.getSession()
                                         .getAttribute("kakaoId");
        Long userId = userService.getLoginUser(kakaoId)
                                 .getId();
        Post post = postService.findPostById(postId);

        if (userId.equals(post.getAuthor().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("내가 작성한 글은 스크랩할 수 없습니다.");
        }

        postService.unscrap(userId, postId);
        return ResponseEntity.ok()
                             .body("스크랩을 해제했습니다.");
    }
}
