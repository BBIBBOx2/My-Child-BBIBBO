package com.publicapi.test.domain.user.controller;

import com.publicapi.test.domain.oauth.service.OauthService;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        return "redirect:/hospital";
    }

    @GetMapping("/logout")
    public String loginUser(HttpSession session) {
        session.removeAttribute("kakaoId");
        return "redirect:/hospital";
    }

    @GetMapping("/user/signup")
    public String signupForm(HttpSession session, Model model) {
        model.addAttribute("userId", session.getAttribute("kakaoId").toString());
        return "user/signup";
    }

    @PostMapping("/register/{userId}")
    public String handleRegistrationForm(HttpSession session,
                                         @RequestParam("name") String name,
                                         @RequestParam("nickname") String nickname,
                                         @RequestParam("imgFile") MultipartFile imgFile,
                                         @RequestParam("email") String email,
                                         Model model) {
        String userId = (String) session.getAttribute("kakaoId");
        userService.updateUser(userId, name, nickname, email, imgFile);
        log.info(userId);
        log.info(name);
        log.info(nickname);

        return "redirect:/hospital";
    }



}






