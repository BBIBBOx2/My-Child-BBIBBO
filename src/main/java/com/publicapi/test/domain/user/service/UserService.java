package com.publicapi.test.domain.user.service;

import com.publicapi.test.domain.user.dto.UserInfo;
import com.publicapi.test.domain.user.dto.UserMapper;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 현재 로그인 사용자
     */
    public UserEntity getLoginUser(String kakaoId) {
        UserEntity loginUser = findUserByKakaoId(kakaoId);
        return loginUser;
    }

    private UserEntity findUserByKakaoId(String kakaoId) {
        Optional<UserEntity> user = userRepository.findByKakaoId(kakaoId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
    }

    public Boolean isUserRegister(String kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }

    public void loginUser(String kakaoId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("kakaoId", kakaoId);
    }

    public void registerUser(String kakaoId, HttpServletRequest request) {
        UserEntity user = null;
        user = UserEntity.builder()
                .kakaoId(kakaoId)
                .email("abc@naver.com")
                .name("이름")
                .username("닉네임")
                .build();
        userRepository.save(user);
        loginUser(kakaoId, request);
    }

    public void updateUser(String userId, String name, String nickname, String email, MultipartFile imgFile) {
        Optional<UserEntity> user = userRepository.findByKakaoId(userId);
        UserInfo userInfo=UserInfo.builder().username(nickname).name(name).email(email).build();
        userMapper.update(user.get(), userInfo);
    }
}
