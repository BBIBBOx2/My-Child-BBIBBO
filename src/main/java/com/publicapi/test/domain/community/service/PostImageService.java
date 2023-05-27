package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.PostImage;
import com.publicapi.test.domain.community.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {

    private final PostImageRepository postImageRepository;

    public void saveImage(Long postId, String imageUrl) {
        PostImage postImage = new PostImage();
        postImage.setPostId(postId);
        postImage.setImage(imageUrl);
        postImageRepository.save(postImage);
        postImageRepository.flush();
    }

    public List<PostImage> findPostImageByPostId(Long postId) {
        Optional<List<PostImage>> postImages = postImageRepository.findPostImageByPostId(postId);

        if (postImages.isPresent()) {
            return postImages.get();
        }

        return Collections.emptyList();
    }
}
