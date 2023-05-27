package com.publicapi.test.domain.user.controller;

import com.publicapi.test.domain.oauth.service.OauthService;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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




}
