package com.publicapi.test.domain.user.service;

import com.publicapi.test.domain.user.dto.UserInfo;
import com.publicapi.test.domain.user.dto.UserMapper;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Boolean signUp(UserInfo userInfo, String kakaoId) {
        Optional<UserEntity> user = userRepository.findByKakaoId(kakaoId);

        userMapper.update(user.get(), userInfo);

        return true;

    }

    /**
     * 현재 로그인 사용자
     */
    public UserEntity getLoginUser(String kakaoId) {
        UserEntity loginUser = userRepository.findByKakaoId(kakaoId).get();
        return loginUser;
    }

    public void registerUser(String kakaoId, HttpServletRequest request) {

        UserEntity user = null;
        UserInfo userInfo=UserInfo.builder().username("닉닉네임").name("이이름").email("abc@naver.com").build();

        if (!userRepository.existsByKakaoId(kakaoId)) {
            user = UserEntity.builder().kakaoId(kakaoId).email("abc@naver.com").name("이름").username("닉네임").build();
            userRepository.save(user);
            Boolean isSignup = signUp(userInfo, kakaoId);
            System.out.println("isSignup = " + isSignup);
        } else {
            user = userRepository.findByKakaoId(kakaoId).get();
        }
        HttpSession session = request.getSession();
        session.setAttribute("kakaoId", kakaoId);

    }

}
