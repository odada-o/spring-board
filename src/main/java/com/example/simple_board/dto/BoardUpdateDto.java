package com.example.simple_board.dto;

/**
 * 게시글 수정 요청 DTO
 */
public class BoardUpdateDto {
    
    private String title;
    private String content;
    private String author;
    
    public BoardUpdateDto() {}
    
    public BoardUpdateDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}