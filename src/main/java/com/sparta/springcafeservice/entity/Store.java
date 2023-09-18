package com.sparta.springcafeservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "stores")
@NoArgsConstructor
public class Store extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storeName")
    private String storeName; // 가게 이름

    @Column(name = "storeAddress")
    private String storeAddress; // 가게 주소

    @Column(name = "information")
    private String information; //가게 정보

//    @JsonIgnore
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store", orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    public Store(StoreRequestDto requestDto, User user) {
        this.storeName = requestDto.getStoreName();
        this.storeAddress = requestDto.getStoreAddress();
        this.information = requestDto.getInformation();
        this.user = user;
    }

    public void update(StoreRequestDto requestDto) {
        this.storeName = requestDto.getStoreName();
        this.storeAddress = requestDto.getStoreAddress();
        this.information = requestDto.getInformation();
    }

    public void addReviewList(Review comment) {
        // Comment 객체를 Store와 연결
        comment.setStore(this); // Comment 엔티티에 있는 setStore 메서드를 활용하여 연결

        // Comment 객체를 Store의 commentList에 추가
    }
}

