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

    // CREATE - 리뷰 작성
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        // 가게 존재 여부 확인
        Store store = findStore(commentRequestDto.getStoreId());

        // Entity로 <- RequestDto를
        Comment comment = new Comment(commentRequestDto, user, store);

        // DB 저장
        Comment savedComment = commentRepository.save(comment);

        // 저장된 리뷰를 가게의 리뷰 리스트에 추가
        store.addCommentList(comment);
        System.out.println("리뷰가 등록되었습니다.");

        return new CommentResponseDto(savedComment);
    }

    // READ All - 리뷰 조회
    public List<CommentResponseDto> getAllComments(User user) {
        // 모든 리뷰을 데이터베이스에서 조회
        List<Comment> commentList = commentRepository.findAll();
        System.out.println("모든 리뷰 조회 완료.");

        // 조회된 모든 리뷰을 CommentResponseDto로 매핑하고 리스트로 반환 (row -> 객체 -> 객체배열(list))
        return commentList.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    // READ ONE - 리뷰 선택 조회
    public CommentResponseDto getComment(Long id, User user) {
        // 특정 리뷰 DB 존재 여부 확인
        Comment comment = findComment(id);
        System.out.println("리뷰번호"+id+"번 리뷰 조회 완료.");

        // 조회된 리뷰를 CommentResponseDto로 매핑하여 반환
        return new CommentResponseDto(comment);
    }

    // UPDATE - 선택 리뷰 수정
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        // 특정 리뷰 DB 존재 여부 확인
        Comment comment = findComment(id);

        // 리뷰의 작성자와 현재 로그인한 사용자를 비교하여 작성자가 같지 않으면 예외 발생
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("리뷰 작성자만 수정할 수 있습니다.");
        }

        // 리뷰을 업데이트
        comment.update(requestDto);
        System.out.println("리뷰번호"+id+"번 리뷰 수정 완료.");

        // 업데이트된 리뷰을 반환
        return new CommentResponseDto(comment);
    }

    // DELETE - 선택 리뷰 삭제
    @Transactional
    public ResponseEntity<String> deleteComment(Long id, User user) {
        // 특정 리뷰 DB 존재 여부 확인
        Comment comment = findComment(id);

        // DB기록된 리뷰의 작성자와 현재 로그인한 사용자를 비교하여 작성자가 같지 않으면 예외 발생
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("리뷰 작성자만 삭제할 수 있습니다.");
        }

        // 리뷰를 삭제
        commentRepository.delete(comment);
        System.out.println("리뷰번호"+id+"번 리뷰 삭제 완료.");

        // 삭제가 성공한 응답을 반환
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }


    // find1 - 리뷰 ID로 존재 여부 확인
    private Store findStore(Long id) {
        // 가게 ID를 사용하여 특정 가게을 조회하고, 존재하지 않을 경우 예외 발생
        return storeRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 가게는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }

    // find2 - 리뷰 ID로 존재 여부 확인
    private Comment findComment(Long id) {
        // 리뷰 ID를 사용하여 특정 리뷰를 조회하고, 존재하지 않을 경우 예외 발생
        return commentRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 리뷰는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }

}