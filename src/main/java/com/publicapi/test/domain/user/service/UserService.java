package com.publicapi.test.domain.user.service;

import com.publicapi.test.domain.community.exception.NotFoundException;
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


    public void registerUser(String userId, String name, String nickname, String email, String region, String bornYear, MultipartFile imgFile) {
        bornYear=bornYear.substring(0, bornYear.length());
        Integer intYear = Integer.parseInt(bornYear);
        UserEntity user=UserEntity.builder()
                .kakaoId(userId)
                .email(email)
                .name(name)
                .region(region)
                .bornYear(intYear)
                .username(nickname)
                .build();
        userRepository.save(user);

    }

    public void updateUser(UserInfo userInfo) {
        Optional<UserEntity> user = userRepository.findByEmail(userInfo.getEmail());
        userMapper.update(user.get(), userInfo);
    }

    public void updateUser(String kakaoId, String username, String profileImage) {
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                                        .orElseThrow(() -> new NotFoundException("사용자를 찾지 못했습니다."));
        if (profileImage == null) {
            profileImage = user.getProfileImage();
        }
        UserInfo userInfo = UserInfo.builder()
                                    .username(username)
                                    .profileImage(profileImage)
                                    .name(user.getName())
                                    .email(user.getEmail())
                                    .build();

        userMapper.update(user, userInfo);
    }
}
