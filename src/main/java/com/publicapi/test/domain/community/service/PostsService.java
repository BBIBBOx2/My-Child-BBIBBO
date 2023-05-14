package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.Posts;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    public Page<Posts> findAllPosts(int page, int boardId, String search, int district, String sortType) {
        String sortCategory = mappingSort(sortType);
        Sort sort = Sort.by(Sort.Direction.DESC, sortCategory);
        Pageable pageable = PageRequest.of(page, 10, sort);
        Specification<Posts> spec = searchSpecification(boardId, search, district);
        Page<Posts> posts = postsRepository.findAll(spec, pageable);
        return posts;
    }

    private String mappingSort(String sortType) {
        if (sortType.equals("popular")) {
            return "hits";
        }
        return "createDate";
    }

    private Specification<Posts> searchSpecification(int boardId, String search, int district) {
        return (posts, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicateBoard = criteriaBuilder.equal(posts.get("boards"), boardId);

            Predicate predicateSearch = criteriaBuilder.or(
                    criteriaBuilder.like(posts.get("title"), "%" + search + "%"),
                    criteriaBuilder.like(posts.get("content"), "%" + search + "%")
            );

            if (district == 0) {
                return criteriaBuilder.and(predicateBoard, predicateSearch);
            }


            Predicate predicateDistinct = criteriaBuilder.equal(posts.get("districts"), district);
            return criteriaBuilder.and(predicateBoard, predicateSearch, predicateDistinct);
        };
    }

    public Posts findPostById(Long postId) {
        Optional<Posts> post = postsRepository.findById(postId);
        if (post.isPresent()) {
            return post.get();
        }
        throw new NotFoundException("게시물을 찾을 수 없습니다.");
    }
}
