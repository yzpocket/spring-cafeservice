package com.sparta.springcafeservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "store")
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
//    private List<Menu> menuList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
//    private List<Comment> commentList = new ArrayList<>();

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
}