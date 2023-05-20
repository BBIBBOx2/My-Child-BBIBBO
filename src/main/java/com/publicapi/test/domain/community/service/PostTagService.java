package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.PostTag;
import com.publicapi.test.domain.community.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostTagService {

    private final PostTagRepository postTagRepository;

    public void create(Long postId, List<Long> hashtagIds) {
        for (Long hashtagId : hashtagIds) {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setHashtagId(hashtagId);
            postTagRepository.save(postTag);
            postTagRepository.flush();
        }
    }
}
