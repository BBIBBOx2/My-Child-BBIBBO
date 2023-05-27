package com.publicapi.test.domain.user.repository;

import com.publicapi.test.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByKakaoId(String kakaoId);

    Optional<UserEntity> findByKakaoId(String kakaoId);

}
