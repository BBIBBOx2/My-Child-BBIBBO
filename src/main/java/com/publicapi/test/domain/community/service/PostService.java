package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.dto.PostRequest;
import com.publicapi.test.domain.community.entity.Board;
import com.publicapi.test.domain.community.entity.District;
import com.publicapi.test.domain.community.entity.Post;
import com.publicapi.test.domain.community.entity.User;
import com.publicapi.test.domain.community.exception.NotFoundException;
import com.publicapi.test.domain.community.repository.BoardRepository;
import com.publicapi.test.domain.community.repository.DistrictRepository;
import com.publicapi.test.domain.community.repository.PostRepository;
import com.publicapi.test.domain.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final DistrictRepository districtRepository;
    private final UserRepository userRepository;

    public Page<Post> findAllPosts(int page, int boardId, String search, int district, String sortType) {
        String sortCategory = mappingSort(sortType);
        Sort sort = Sort.by(Sort.Direction.DESC, sortCategory);
        Pageable pageable = PageRequest.of(page, 10, sort);
        Specification<Post> spec = searchSpecification(boardId, search, district);
        Page<Post> posts = postRepository.findAll(spec, pageable);
        return posts;
    }

    private String mappingSort(String sortType) {
        if (sortType.equals("popular")) {
            return "hits";
        }
        return "createDate";
    }

    private Specification<Post> searchSpecification(int boardId, String search, int district) {
        return (post, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate predicateBoard = criteriaBuilder.equal(post.get("board"), boardId);

            Predicate predicateSearch = criteriaBuilder.or(
                    criteriaBuilder.like(post.get("title"), "%" + search + "%"),
                    criteriaBuilder.like(post.get("content"), "%" + search + "%")
            );

            if (district == 0) {
                return criteriaBuilder.and(predicateBoard, predicateSearch);
            }


            Predicate predicateDistinct = criteriaBuilder.equal(post.get("district"), district);
            return criteriaBuilder.and(predicateBoard, predicateSearch, predicateDistinct);
        };
    }

    public Post findPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            return post.get();
        }
        throw new NotFoundException("게시물을 찾을 수 없습니다.");
    }



    public Long create(PostRequest postRequest) {
        Board board = boardRepository.findById(postRequest.getBoardId())
                                     .orElseThrow(() -> new NotFoundException("해당 게시판을 찾지 못했습니다."));
        District district = districtRepository.findById(postRequest.getDistrictId())
                                              .orElseThrow(() -> new NotFoundException("해당 게시판을 찾지 못했습니다."));
        User user = userRepository.findById(postRequest.getUserId())
                                  .orElseThrow(() -> new NotFoundException("해당 게시판을 찾지 못했습니다."));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setBoard(board);
        post.setDistrict(district);
        post.setAuthor(user);
        post.setIsAnonymous(postRequest.getIsAnonymous());
        post.setHits(0);
        post.setCreateDate(LocalDateTime.now());
        this.postRepository.save(post);
        this.postRepository.flush();
        long id = post.getId();

        return id;
    }
}
