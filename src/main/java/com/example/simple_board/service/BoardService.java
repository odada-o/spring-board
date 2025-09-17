package com.example.simple_board.service;

import com.example.simple_board.entity.Board;
import com.example.simple_board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 게시판 비즈니스 로직 서비스
 */
@Service
@Transactional(readOnly = true)
public class BoardService {
    
    private final BoardRepository boardRepository;
    
    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    
    /**
     * 페이징된 게시글 목록 조회
     */
    public Page<Board> getBoardsWithPaging(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return boardRepository.findAll(pageable);
    }
    
    /**
     * ID로 게시글 조회 (조회수 증가)
     */
    @Transactional
    public Optional<Board> getBoardById(Long id) {
        Optional<Board> boardOpt = boardRepository.findById(id);
        
        boardOpt.ifPresent(board -> {
            board.setViewCount(board.getViewCount() + 1);
            boardRepository.save(board);
        });
        
        return boardOpt;
    }
    
    /**
     * 게시글 작성
     */
    @Transactional
    public Board createBoard(Board board) {
        board.setViewCount(0);
        return boardRepository.save(board);
    }
    
    /**
     * 게시글 수정
     */
    @Transactional
    public Optional<Board> updateBoard(Long id, Board updatedBoard) {
        return boardRepository.findById(id)
            .map(existingBoard -> {
                existingBoard.setTitle(updatedBoard.getTitle());
                existingBoard.setContent(updatedBoard.getContent());
                existingBoard.setAuthor(updatedBoard.getAuthor());
                return boardRepository.save(existingBoard);
            });
    }
    
    /**
     * 게시글 삭제
     */
    @Transactional
    public boolean deleteBoard(Long id) {
        if (boardRepository.existsById(id)) {
            boardRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 게시글 검색
     */
    public List<Board> searchBoards(String keyword) {
        return boardRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
}