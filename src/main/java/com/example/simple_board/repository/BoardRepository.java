package com.example.simple_board.repository;

import com.example.simple_board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Board 엔티티를 위한 Repository 인터페이스
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    /**
     * 제목으로 게시글 검색
     */
    List<Board> findByTitleContaining(String title);
    
    /**
     * 작성자로 게시글 검색
     */
    List<Board> findByAuthor(String author);
    
    /**
     * 제목 또는 내용으로 게시글 검색
     */
    List<Board> findByTitleContainingOrContentContaining(String title, String content);
    
    /**
     * 조회수 기준으로 상위 게시글 조회
     */
    @Query("SELECT b FROM Board b ORDER BY b.viewCount DESC LIMIT :limit")
    List<Board> findTopByViewCount(@Param("limit") int limit);
    
    /**
     * 최신 게시글 조회
     */
    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC LIMIT :limit")
    List<Board> findLatestPosts(@Param("limit") int limit);
}