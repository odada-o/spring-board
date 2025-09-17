package com.example.simple_board.controller;

import com.example.simple_board.dto.BoardCreateDto;
import com.example.simple_board.dto.BoardUpdateDto;
import com.example.simple_board.entity.Board;
import com.example.simple_board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 게시판 REST API 컨트롤러
 * 게시글 CRUD 및 검색 기능 제공
 */
@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = "*")
public class BoardController {
    
    private final BoardService boardService;
    
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    
    /**
     * 게시글 목록 조회 (페이징 지원)
     */
    @GetMapping
    public ResponseEntity<Page<Board>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<Board> boards = boardService.getBoardsWithPaging(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(boards);
    }
    
    /**
     * 게시글 상세 조회 (조회수 증가)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        Optional<Board> board = boardService.getBoardById(id);
        
        if (board.isPresent()) {
            return ResponseEntity.ok(board.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 게시글 작성
     */
    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody BoardCreateDto boardCreateDto) {
        Board board = new Board(
            boardCreateDto.getTitle(),
            boardCreateDto.getContent(),
            boardCreateDto.getAuthor()
        );
        
        Board createdBoard = boardService.createBoard(board);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }
    
    /**
     * 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Board> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardUpdateDto boardUpdateDto) {
        
        Board updateBoard = new Board(
            boardUpdateDto.getTitle(),
            boardUpdateDto.getContent(),
            boardUpdateDto.getAuthor()
        );
        
        Optional<Board> updatedBoard = boardService.updateBoard(id, updateBoard);
        
        if (updatedBoard.isPresent()) {
            return ResponseEntity.ok(updatedBoard.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boolean deleted = boardService.deleteBoard(id);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 게시글 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<Board>> searchBoards(@RequestParam String keyword) {
        List<Board> boards = boardService.searchBoards(keyword);
        return ResponseEntity.ok(boards);
    }
}