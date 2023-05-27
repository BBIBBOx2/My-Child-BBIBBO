package com.publicapi.test.domain.user.controller;

import com.publicapi.test.domain.oauth.service.OauthService;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import javax.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final OauthService oauthService;
    private final UserService userService;


    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletRequest request) {
        System.out.println("code = " + code);

        String accessToken = oauthService.getAccessToken(code);
        String kakaoId = oauthService.getUserIdByToken(accessToken);
        userService.registerUser(kakaoId, request);

        return "redirect:/hospital";

    }

    @GetMapping("/now")
    public String loginUser(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("kakaoId");
        UserEntity user = userService.getLoginUser(id);
        System.out.println("user = " + user.getName());
        return "redirect:/hospital";
    }

    @GetMapping("/logout")
    public String loginUser(HttpSession session) {
        session.removeAttribute("kakaoId");
        return "redirect:/hospital";
    }

    @GetMapping("/user/signup/{userId}")
    public String signupForm(@PathVariable("userId") String userId, Model model) {
        model.addAttribute("userId", userId);
        return "user/signup";
    }

    @PostMapping("/register/{userId}")
    public String handleRegistrationForm(@PathVariable("userId") String userId,
                                         @RequestParam("name") String name,
                                         @RequestParam("nickname") String nickname,
                                         @RequestParam("imgFile") MultipartFile imgFile,
                                         Model model) {

        log.info(userId);
        log.info(name);
        log.info(nickname);

        return "redirect:/hospital";
    }



}






