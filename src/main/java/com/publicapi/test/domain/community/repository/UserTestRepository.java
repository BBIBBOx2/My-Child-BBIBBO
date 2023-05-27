package com.publicapi.test.domain.community.repository;

import com.publicapi.test.domain.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTestRepository extends JpaRepository<User, Long> {
}
