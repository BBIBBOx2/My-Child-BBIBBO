package com.publicapi.test.domain.community.repository;

import com.publicapi.test.domain.community.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findByAuthorId(Long id, Pageable pageable);
    Page<Posts> findAll(Pageable pageable);
    Page<Posts> findAll(Specification<Posts> spec, Pageable pageable);
}
