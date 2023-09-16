package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.CommentRequestDto;
import com.sparta.springcafeservice.dto.CommentResponseDto;
import com.sparta.springcafeservice.entity.Comment;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 작성 (refactor : API response Type change)
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long userId, Long storeId) {
        // RequestDto -> Entity
        Comment comment = new Comment(commentRequestDto, userId, storeId);

        try {
            // DB 저장
            Comment saveComment = commentRepository.save(comment);
            // Entity -> ResponseDto를 바로 반환
            return new CommentResponseDto(saveComment);
        } catch (DataAccessException ex) {
            // 데이터베이스 예외 처리
            throw new RestApiException("댓글 작성 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    // 리뷰 조회 (refactor : API response Type change)
    public List<CommentResponseDto> getComments() {
        try {
            List<Comment> commentList = commentRepository.findAll(); // 엔티티 목록을 가져옴
            return commentList.stream()
                    .map(CommentResponseDto::new) // Dto 객체로 변환
                    .collect(Collectors.toList()); // DTO 목록을 반환
        } catch (DataAccessException ex) {
            throw new RestApiException("댓글 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    // 선택 댓글 수정 by userId (refactor : API response Type change)
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, Long userId) {
        Comment comment = findComment(id);
        if (comment.getUserId().equals(userId)) {
            comment.update(commentRequestDto); // 변경 감지가 적용됨
            return new CommentResponseDto(comment);
        } else {
            throw new RestApiException("당신에겐 댓글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN.value());
        }
    }

    // 작성자가 댓글 삭제
    @Transactional
    public void deleteComment(Long id, String userId) {
        Comment comment = findComment(id); // 댓글 존재 확인 검증 메소드
        if (comment.getUserId().equals(userId)) {
            commentRepository.delete(comment); // 변경 감지가 적용됨
        } else {
            throw new RestApiException("당신에겐 댓글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN.value());
        }
    }


    // 댓글 ID로 존재 여부 확인 공용메서드
    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 댓글은 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }
}