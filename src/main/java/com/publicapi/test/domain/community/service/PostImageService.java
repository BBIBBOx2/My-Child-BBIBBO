package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.PostImage;
import com.publicapi.test.domain.community.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {

    private final PostImageRepository postImageRepository;

    public void saveImage(Long postId, String imagePath) {
        PostImage postImage = new PostImage();
        postImage.setPostId(postId);
        postImage.setImage(imagePath);
        postImageRepository.save(postImage);
        postImageRepository.flush();
    }
}
