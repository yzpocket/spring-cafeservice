package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

// 클라이언트에 반환할 Dto
@Getter
public class CommentResponseDto {  // response Dto
    private Long id;
    private Long storeId;
    private Long userId;
    private byte star;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 리뷰 저장용 Dto 생성자
    public CommentResponseDto(Comment updatedComment) {
        this.id = updatedComment.getId();
        this.storeId = updatedComment.getStoreId();
        this.userId = updatedComment.getUserId();
        this.star = updatedComment.getStar();
        this.comment = updatedComment.getComment();
        this.createdAt = updatedComment.getCreatedAt();
        this.modifiedAt = updatedComment.getModifiedAt();
    }

    // 리뷰 업데이트용 생성자
    //public CommentResponseDto(CommentResponseDto updatedComment) {
    //    this.id = updatedComment.getId();
    //    this.storeId = updatedComment.getStoreId();
    //    this.userId = updatedComment.getUserId();
    //    this.star = updatedComment.getStar();
    //    this.comment = updatedComment.getComment();
    //    this.createdAt = updatedComment.getCreatedAt();
    //    this.modifiedAt = updatedComment.getModifiedAt();
    //}

    // CommentResponseDto 형태로 변환
    //public CommentResponseDto fromComment(Comment comment) {
    //    return new CommentResponseDto(comment.getId(), comment.getStoreId(), comment.getUserId(), comment.getStar(), comment.getComment(), comment.getCreatedAt(), comment.getModifiedAt());
    //}
}