package com.publicapi.test.domain.user.service;

import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
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

    /**
     * 현재 로그인 사용자
     */
    public Optional<UserEntity> getLoginOptionalUser(String kakaoId) {
        Optional<UserEntity> loginUser = userRepository.findByKakaoId(kakaoId);
        return loginUser;
    }

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

    public void loginTestUser(HttpServletRequest request) {
        String testId = "-1486491455";
        HttpSession session = request.getSession();
        session.setAttribute("kakaoId", testId);
    }

    public void registerUser(String userId, String name, String nickname, String email, RegionEntity region, String bornYear, String imageUrl) {
        bornYear=bornYear.substring(0, bornYear.length());
        Integer intYear = Integer.parseInt(bornYear);
        UserEntity user=UserEntity.builder()
                .kakaoId(userId)
                .email(email)
                .name(name)
                .region(region)
                .bornYear(intYear)
                .username(nickname)
                .profileImage(imageUrl)
                .build();
        userRepository.save(user);

    }

    public void updateUser(String kakaoId, String username, RegionEntity region, String bornYear, String profileImage) {
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                                        .orElseThrow(() -> new NotFoundException("사용자를 찾지 못했습니다."));
        bornYear=bornYear.substring(0, bornYear.length());
        Integer intYear = Integer.parseInt(bornYear);
        if (profileImage == null) {
            profileImage = user.getProfileImage();
        }
        UserInfo userInfo = UserInfo.builder()
                                    .username(username)
                                    .profileImage(profileImage)
                                    .name(user.getName())
                                    .region(region)
                                    .bornYear(intYear)
                                    .email(user.getEmail())
                                    .build();

        userMapper.update(user, userInfo);
    }
}
