package com.publicapi.test.domain.community.controller;

import com.publicapi.test.domain.community.dto.PostRequest;
import com.publicapi.test.domain.community.entity.Board;
import com.publicapi.test.domain.community.entity.District;
import com.publicapi.test.domain.community.entity.Post;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.repository.BoardRepository;
import com.publicapi.test.domain.community.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("community")
public class CommunityController {

    private final PostService postService;
    private final HashtagService hashtagService;
    private final PostTagService postTagService;
    private final PostImageService postImageService;
    private final DistrictService districtService;
    private final BoardRepository boardRepository;

    @GetMapping("{boardId}")
    public String showPosts(Model model,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @PathVariable int boardId,
                            @RequestParam(value = "search", defaultValue = "") String search,
                            @RequestParam(value = "district", defaultValue = "0") int district,
                            @RequestParam(value = "sort", defaultValue = "recent") String sort) {
        Page<Post> postPage = postService.findAllPosts(page, boardId, search, district, sort);

        if (postPage == null) {
            throw new NotFoundException("게시물들을 찾을 수 없습니다.");
        }

        model.addAttribute("postPage", postPage);
        model.addAttribute("board", boardId);
        model.addAttribute("search", search);
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("sort", sort);
        return "community/community_list";
    }

    @GetMapping("{boardId}/write")
    public String loadWriteForm(Model model,
                                @PathVariable int boardId) {
        List<Board> boards = boardRepository.findAll();
        List<District> districts = districtService.findAll();

        model.addAttribute("boardId", boardId);
        model.addAttribute("boards", boards);
        model.addAttribute("districts", districts);

        return "community/community_write";
    }

    @PostMapping("{boardId}/write")
    @ResponseBody
    public String savePost(@RequestPart(name = "contentData") PostRequest postRequest,
                           @RequestPart(name = "images", required = false) List<MultipartFile> images) {
        Long postId = postService.create(postRequest);
        if (postRequest.getHashtags() != null) {
            List<Long> hashtagIds = hashtagService.create(postRequest.getHashtags());
            postTagService.create(postId, hashtagIds);
        }
        savePostImage(postId, images);

        return "/community/" + postRequest.getBoardId();
    }

    private void savePostImage(Long postId, List<MultipartFile> images) {
        Path uploadRoot = Paths.get(System.getProperty("user.home"))
                               .resolve("bbibbo_storage");

        if (images != null) {
            for (MultipartFile image : images) {
                UUID uuid = UUID.randomUUID();
                Path uploadPath = uploadRoot.resolve(uuid + ".png");

                try {
                    image.transferTo(uploadPath);
                    postImageService.saveImage(postId, uploadPath.toString());
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
