package com.sparta.springcafeservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity @Getter
@Table(name = "stores")
@NoArgsConstructor
public class Store extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storeName")
    @Pattern(regexp = "^[\\s\\S]{1,20}$", message = "가게 이름은 20자 이내로 입력해야 합니다.")
    private String storeName; // 가게 이름

    @Column(name = "storeAddress")
    private String storeAddress; // 가게 주소

    @Column(name = "information")
    private String information; //가게 정보

    @Column(name = "businessNum")
    @Pattern(regexp = "^[0-9]{10}$", message = "사업자 번호는 10자리 숫자로 입력해야 합니다.")
    private String businessNum; //사업자 번호

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Menu> menuList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviewList = new ArrayList<>();

    public Store(StoreRequestDto requestDto, User user) {
        this.storeName = requestDto.getStoreName();
        this.storeAddress = requestDto.getStoreAddress();
        this.information = requestDto.getInformation();
        this.businessNum = requestDto.getBusinessNum();
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
}
