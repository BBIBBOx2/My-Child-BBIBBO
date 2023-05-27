package com.publicapi.test.domain.community.repository;

import com.publicapi.test.domain.community.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    Optional<List<PostImage>> findPostImageByPostId(Long id);
}
