package com.publicapi.test.domain.community.controller;

import com.publicapi.test.domain.community.dto.PostRequest;
import com.publicapi.test.domain.community.entity.*;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.service.*;
import com.publicapi.test.s3.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("community")
public class CommunityController {

    private final PostService postService;
    private final HashtagService hashtagService;
    private final PostTagService postTagService;
    private final PostImageService postImageService;
    private final DistrictService districtService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final ImageUploadService imageUploadService;

    @GetMapping()
    public String mainPage() {
        return "redirect:/community/1";
    }

    @GetMapping("{boardId}")
    public String showPosts(Model model,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @PathVariable int boardId,
                            @RequestParam(value = "search", defaultValue = "") String search,
                            @RequestParam(value = "district", defaultValue = "0") int districtType,
                            @RequestParam(value = "sortType", defaultValue = "recent") String sortType) {
        Page<Post> postPage = postService.findAllPosts(page, boardId, search, districtType, sortType);
        if (postPage == null) {
            throw new NotFoundException("게시물들을 찾을 수 없습니다.");
        }

        List<District> districts = districtService.findAll();

        model.addAttribute("postPage", postPage);
        model.addAttribute("board", boardId);
        model.addAttribute("search", search);
        model.addAttribute("districts", districts);
        model.addAttribute("districtType", districtType);
        model.addAttribute("sortType", sortType);
        return "community/community_list";
    }

    @GetMapping("{boardId}/{postId}")
    public String showPost(Model model,
                           @PathVariable Long boardId, @PathVariable Long postId) {
        Post post = postService.findPostById(postId);
        List<PostImage> postImages = postImageService.findPostImageByPostId(postId);
        List<Comment> comments = commentService.findAllByPostId(postId);
        List<PostTag> postTags = postTagService.findAllByPostId(postId);

        model.addAttribute("board", boardId);
        model.addAttribute("post", post);
        model.addAttribute("postImages", postImages);
        model.addAttribute("comments", comments);
        model.addAttribute("postTags", postTags);
        return "community/community_detail";
    }

    @GetMapping("{boardId}/write")
    public String loadWriteForm(Model model,
                                @PathVariable int boardId) {
        List<Board> boards = boardService.findAll();
        List<District> districts = districtService.findAll();

        model.addAttribute("boardId", boardId);
        model.addAttribute("boards", boards);
        model.addAttribute("districts", districts);

        return "community/community_write";
    }

    @PostMapping("{boardId}/write")
    @ResponseBody
    public String savePost(HttpServletRequest request,
                           @RequestPart(name = "contentData") PostRequest postRequest,
                           @RequestPart(name = "images", required = false) List<MultipartFile> images) {
        String kakaoId = (String) request.getSession().getAttribute("kakaoId");
        Long postId = postService.create(postRequest, kakaoId);
        if (postRequest.getHashtags() != null) {
            List<Long> hashtagIds = hashtagService.create(postRequest.getHashtags());
            postTagService.create(postId, hashtagIds);
        }
        if (images != null) {
            savePostImage(postId, images);
        }

        return "/community/" + postRequest.getBoardId();
    }

    private void savePostImage(Long postId, List<MultipartFile> images) {
        for (MultipartFile image : images) {
            String imageUrl = imageUploadService.uploadImage(image);
            postImageService.saveImage(postId, imageUrl);
        }
    }

    @GetMapping("{boardId}/{postId}/scrap")
    public String scrapPost(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId) {
        Long userId = 1L;
        postService.scrap(userId, postId);

        return String.format("redirect:/community/%d/%d", boardId, postId);
    }
}
