package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.Hashtag;
import com.publicapi.test.domain.community.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public List<Long> create(List<String> hashtags) {
        List<Long> ids = new ArrayList<>();
        for (String value : hashtags) {
            Optional<Long> hashtagId = findHashtag(value);
            if (hashtagId.isPresent()) {
                ids.add(hashtagId.get());
                continue;
            }

            Hashtag hashTag = new Hashtag();
            hashTag.setName(value);
            hashtagRepository.save(hashTag);
            hashtagRepository.flush();
            long id = hashTag.getId();
            ids.add(id);
        }

        return ids;
    }

    private Optional<Long> findHashtag(String hashtag) {
        Optional<Hashtag> findHashtag = hashtagRepository.findByName(hashtag);
        if (findHashtag.isPresent()) {
            return Optional.of(findHashtag.get().getId());
        }
        return Optional.empty();
    }
}
