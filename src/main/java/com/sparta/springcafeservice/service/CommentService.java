package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.CommentRequestDto;
import com.sparta.springcafeservice.dto.CommentResponseDto;
import com.sparta.springcafeservice.entity.Comment;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.CommentRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final StoreRepository storeRepository;

    // CREATE - 댓글 작성
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        Store store = storeRepository.findById(commentRequestDto.getStoreId()).orElseThrow(() ->
                new IllegalArgumentException("선택한 가게는 존재하지 않습니다.")
        );
        log.info(String.valueOf(commentRequestDto.getStoreId()));
        log.info(String.valueOf(storeRepository.findById(commentRequestDto.getStoreId())));
        //RequestDto -> Entity
        Comment comment = new Comment(commentRequestDto, user, store);
        //DB 저장
        Comment saveComment = commentRepository.save(comment);
        log.info(String.valueOf(commentRepository.save(comment)));


        store.addCommentList(comment);
        System.out.println("가게에 리뷰가 추가되었습니다.");


        return new CommentResponseDto(saveComment);
    }


    // READ All - 리뷰 조회
    public List<CommentResponseDto> getAllComments(User user) {
        List<Comment> commentList = commentRepository.findAll();
        return commentList.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    // READ 1 - 리뷰 선택 조회
    public CommentResponseDto getComment(Long id, User user) {
        return new CommentResponseDto(findComment(id));
    }


    // UPDATE - 선택 댓글 수정
    @Transactional
    public ResponseEntity<String> updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(id);

        // 댓글의 작성자와 현재 로그인한 사용자를 비교하여 작성자가 같지 않으면 예외 발생
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
        }

        // 패스워드 검증 로직은 댓글에서 사용하지 않으므로 주석 처리

        comment.update(requestDto); // 댓글 업데이트 로직을 호출합니다.

        return ResponseEntity.ok("수정 성공!");
    }

    @Transactional
    public ResponseEntity<String> deleteComment(Long id, User user) {
        Comment comment = findComment(id);

        // 댓글의 작성자와 현재 로그인한 사용자를 비교하여 작성자가 같지 않으면 예외 발생
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);

        return ResponseEntity.ok("삭제 성공!");
    }


    // find 1 - 댓글 ID로 존재 여부 확인
    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 댓글은 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }



}