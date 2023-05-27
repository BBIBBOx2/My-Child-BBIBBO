package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.User;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.repository.UserTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserTestService {

    private final UserTestRepository userTestRepository;

    public User findById(Long id) {
        Optional<User> user = userTestRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }

        throw new NotFoundException("사용자를 찾지 못했습니다.");
    }
}
