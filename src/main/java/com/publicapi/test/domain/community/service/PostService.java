package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.dto.PostRequest;
import com.publicapi.test.domain.community.entity.Board;
import com.publicapi.test.domain.community.entity.Hashtag;
import com.publicapi.test.domain.community.entity.Post;
import com.publicapi.test.domain.community.entity.PostTag;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.repository.BoardRepository;
import com.publicapi.test.domain.community.repository.PostRepository;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
import com.publicapi.test.domain.hospital.repository.RegionRepository;
import com.publicapi.test.domain.user.entity.UserEntity;
import com.publicapi.test.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;

    public Page<Post> findAllPosts(int page, int boardId, String search, long region, String sortType) {
        String sortCategory = mappingSort(sortType);
        Sort sort = Sort.by(Sort.Direction.DESC, sortCategory);
        Pageable pageable = PageRequest.of(page, 10, sort);
        Specification<Post> spec = searchSpecification(boardId, search, region);
        Page<Post> posts = postRepository.findAll(spec, pageable);
        return posts;
    }

    private String mappingSort(String sortType) {
        if (sortType.equals("popular")) {
            return "hits";
        }
        return "createDate";
    }

    private Specification<Post> searchSpecification(int boardId, String search, long region) {
        return (post, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicateBoard = criteriaBuilder.equal(post.get("board"), boardId);
            Predicate predicateSearch = getSearchPredicate(search, post, criteriaBuilder);

            if (region == 0 || region == 1) {
                return criteriaBuilder.and(predicateBoard, predicateSearch);
            }

            Predicate predicateDistinct = criteriaBuilder.equal(post.get("region"), region);
            return criteriaBuilder.and(predicateBoard, predicateSearch, predicateDistinct);
        };
    }

    private Predicate getSearchPredicate(String search, Root<Post> post, CriteriaBuilder criteriaBuilder) {
        if (search.startsWith("#")) {
            search = search.substring(1);
            Join<Post, PostTag> postTagJoin = post.join("postTags", JoinType.INNER);
            Join<PostTag, Hashtag> hashtagJoin = postTagJoin.join("hashtag", JoinType.INNER);
            return criteriaBuilder.equal(hashtagJoin.get("name"), search);
        }

        return criteriaBuilder.or(
                criteriaBuilder.like(post.get("title"), "%" + search + "%"),
                criteriaBuilder.like(post.get("content"), "%" + search + "%")
        );
    }

    public Post findPostById(Long postId) {
        postRepository.updatePostHits(postId);
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            System.out.println();
            return post.get();
        }
        throw new NotFoundException("게시물을 찾을 수 없습니다.");
    }

    public Page<Post> findByUserId(UserEntity user, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Post> posts = postRepository.findByAuthorId(user.getId(), pageable);
        return posts;
    }

    public Page<Post> findByScrapUserid(UserEntity user, int page) {
        Set<Post> scrap = user.getScrap();
        List<Post> sortedScrap = new ArrayList<>(scrap);
        sortedScrap.sort(Comparator.comparing(Post::getCreateDate)
                                   .reversed());
        Pageable pageable = PageRequest.of(page, 10);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min((startIndex + pageable.getPageSize()), sortedScrap.size());
        List<Post> paginatedList = sortedScrap.subList(startIndex, endIndex);
        return new PageImpl<>(paginatedList, pageable, sortedScrap.size());
    }

    public Long create(PostRequest postRequest, String kakaoId) {
        Board board = boardRepository.findById(postRequest.getBoardId())
                                     .orElseThrow(() -> new NotFoundException("해당 게시판을 찾지 못했습니다."));
        RegionEntity region = regionRepository.findById(postRequest.getRegionId())
                                              .orElseThrow(() -> new NotFoundException("해당 지역을 찾지 못했습니다."));
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                                        .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setBoard(board);
        post.setRegion(region);
        post.setAuthor(user);
        post.setIsAnonymous(postRequest.getIsAnonymous());
        post.setHits(0);
        post.setCreateDate(getLocalDateTime());
        this.postRepository.save(post);
        this.postRepository.flush();
        long id = post.getId();

        return id;
    }

    private static LocalDateTime getLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneOffset zoneOffset = ZoneOffset.ofHours(9);
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);
        localDateTime = offsetDateTime.toLocalDateTime();
        return localDateTime;
    }

    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    public void scrap(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new NotFoundException("해당 게시물을 찾을 수 없습니다."));
        UserEntity user = userRepository.findById(userId)
                                        .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));

        post.getScrap()
            .add(user);
        postRepository.save(post);
    }

    public void unscrap(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new NotFoundException("해당 게시물을 찾을 수 없습니다."));
        UserEntity user = userRepository.findById(userId)
                                        .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));

        post.getScrap()
            .remove(user);
        postRepository.save(post);
    }
}
