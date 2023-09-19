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

//  @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();

    //이미 연관관계를 통해서 store테이블의 review 로우들을 리스트로 가져오고 있다.
    //또한 @Getter를 통해서 해당 부분을 가져오고있다.
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

    public List<Menu> getMenuList() {
        return menuList;
    }


    // 이것은 사실상 사용되지 않는다.
    // 메뉴와 비교해보면 사실상 동일한 상황인데, add할필요가 없다.
    //public void addReview(Review review) {
    //    review.setStore(this);
    //
    //    this.reviewList.add(review);
    //}

    //public List<Review> getReviewList() {
    //    return reviewList;
    //}
}
