package com.publicapi.test.domain.community.repository;

import com.publicapi.test.domain.community.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findAllByPostId(Long postId);
}
