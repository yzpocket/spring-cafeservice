package com.sparta.springcafeservice.entity;

import com.sparta.springcafeservice.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment") // 맵핑된 DB 테이블 : comment
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment(MySQL)
    private Long id; // 리뷰 인덱스

    @Column(name = "comment", nullable = false,  columnDefinition = "TEXT") //<- TEXT 타입 in DB
    private String comment; // 리뷰 내용

    @Column(name = "star")
    private byte star; // 리뷰 별점

    //Comment:Store 관계 N:1
    //Comment의 id 컬럼과 Store의 id 컬럼을 Join 조건으로 사용
    //Comment<-Store 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Store store;

    //Comment:User 관계 N:1
    //Comment의 id 컬럼과 User의 id 컬럼을 Join 조건으로 사용
    //Comment<-User 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;


    public Comment(CommentRequestDto commentRequestDto, Long tokenUserId, Long tokenStoreId) {// <- 토큰에서 유저 이름 가져오기
        this.userId = tokenUserId; // Comment와 관련된 유저의 ID를 설정합니다.
        this.storeId = tokenStoreId; // Comment와 관련된 가게의 ID를 설정합니다.
        this.star = commentRequestDto.getStar();
        this.comment = commentRequestDto.getComment();

    }

    // 댓글 수정 생성자
    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}
