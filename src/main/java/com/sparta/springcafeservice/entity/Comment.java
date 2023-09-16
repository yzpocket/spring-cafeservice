package com.sparta.springcafeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    //Comment:User 관계 N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Comment(CommentRequestDto commentRequestDto, User user, Store store) {
        this.comment = commentRequestDto.getComment();
        this.star = commentRequestDto.getStar();
        this.user = user;
        this.store = store; // store 필드 초기화
    }


    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
