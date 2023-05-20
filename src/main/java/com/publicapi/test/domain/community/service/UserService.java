package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.User;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            throw new NotFoundException("사용자를 찾지 못했습니다.");
        }

        return user.get();
    }
}
