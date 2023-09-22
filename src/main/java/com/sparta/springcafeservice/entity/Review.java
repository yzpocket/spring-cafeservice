package com.sparta.springcafeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springcafeservice.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor
@Table(name = "reviews")
public class Review extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  columnDefinition = "TEXT")
    private String review;

    @Column(nullable = false)
    private byte star;

    @JsonIgnore
    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Review(ReviewRequestDto reviewRequestDto, User user, Store store) {
        this.review = reviewRequestDto.getReview();
        this.star = reviewRequestDto.getStar();
        this.user = user;
        this.store = store;
    }

    public void update(ReviewRequestDto requestDto) {
        this.review = requestDto.getReview();
        this.star = requestDto.getStar();
    }
}
