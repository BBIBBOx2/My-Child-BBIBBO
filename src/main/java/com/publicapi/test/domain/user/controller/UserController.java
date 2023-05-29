package com.publicapi.test.domain.user.controller;

import com.publicapi.test.domain.hospital.entity.RegionEntity;
import com.publicapi.test.domain.hospital.repository.RegionRepository;
import com.publicapi.test.domain.oauth.service.OauthService;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.service.UserService;
import com.publicapi.test.s3.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final OauthService oauthService;
    private final UserService userService;
    private final ImageUploadService imageUploadService;
    private final RegionRepository regionRepository;


    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletRequest request) {
        System.out.println("code = " + code);
        String accessToken = oauthService.getAccessToken(code);
        String kakaoId = oauthService.getUserIdByToken(accessToken);
        Boolean isUserRegister = userService.isUserRegister(kakaoId);
        if (isUserRegister) {
            userService.loginUser(kakaoId, request);
            return "hospital";
        } else {
            userService.loginUser(kakaoId, request);
            return "user/signup";
        }
    }

    @GetMapping("/now")
    public String loginUser(HttpServletRequest request) {
        String id = (String) request.getSession()
                                    .getAttribute("kakaoId");
        UserEntity user = userService.getLoginUser(id);
        return "redirect:/hospital";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("kakaoId");
        request.getSession().invalidate();
        return "hospital";
    }

    @GetMapping("/user/signup")
    public String signupForm(HttpServletRequest request, Model model, @RequestParam(required = false) String regionName, @RequestParam(required = false) String bornYear) {
        model.addAttribute("userId", request.getSession().getAttribute("kakaoId")
                                            .toString());

        List<RegionEntity> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        model.addAttribute("regionName", regionName);
        return "user/signup";
    }

    @PostMapping("/register/{userId}")
    public String handleRegistrationForm(HttpServletRequest request,
                                         @RequestParam("name") String name,
                                         @RequestParam("nickname") String nickname,
                                         @RequestParam("region") String region,
                                         @RequestParam("bornYear") String bornYear,
                                         @RequestParam("imgFile") MultipartFile imgFile,
                                         @RequestParam("email") String email,
                                         Model model) {
        String userId = (String) request.getSession().getAttribute("kakaoId");
        String imageUrl = getImageUrl(imgFile);

        userService.registerUser(userId, name, nickname, email, region, bornYear, imageUrl);

        return "hospital";
    }

    @PostMapping("/user/update")
    public String updateUser(HttpServletRequest request,
                           @RequestPart(name = "username") String username,
                           @RequestPart(name = "image", required = false) MultipartFile profileImage) {
        String kakaoId = (String) request.getSession().getAttribute("kakaoId");
        String profileImageUrl = getImageUrl(profileImage);

        userService.updateUser(kakaoId, username, profileImageUrl);

        return "redirect:/mypage/profile";
    }

    private String getImageUrl(MultipartFile profileImage) {
        String profileImageUrl = null;

        if (profileImage != null) {
            profileImageUrl = imageUploadService.uploadImage(profileImage);
        }
        return profileImageUrl;
    }
}
