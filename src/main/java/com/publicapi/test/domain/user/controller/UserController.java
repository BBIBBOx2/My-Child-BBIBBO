package com.publicapi.test.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

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

        return "hospital/hospital-map";
    }

}

