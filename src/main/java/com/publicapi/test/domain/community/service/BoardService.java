package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.Board;
import com.publicapi.test.domain.community.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findAll() {
        return boardRepository.findAll();
    }
}
