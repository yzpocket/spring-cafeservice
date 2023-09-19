package com.sparta.springcafeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springcafeservice.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Review(ReviewRequestDto reviewRequestDto, User user) {
        this.review = reviewRequestDto.getReview();
        this.star = reviewRequestDto.getStar();
        this.user = user;
    }

    public void update(ReviewRequestDto requestDto) {
        this.review = requestDto.getReview();
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
