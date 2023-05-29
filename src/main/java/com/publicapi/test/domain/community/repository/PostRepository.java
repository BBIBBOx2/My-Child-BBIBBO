package com.publicapi.test.domain.community.repository;

import com.publicapi.test.domain.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthorId(Long id, Pageable pageable);
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Modifying
    @Query("UPDATE Post post SET post.hits = (post.hits + 1) WHERE post.id = :postId")
    void updatePostHits(@Param("postId") Long postId);

}
