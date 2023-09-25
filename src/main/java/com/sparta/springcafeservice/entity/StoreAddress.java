package com.sparta.springcafeservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor
public class StoreAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 우편번호
    @Column(nullable = false)
    private int postNum;

    // 시
    @Column(nullable = false)
    private String city;

    // 구
    @Column(nullable = false)
    private String district;

    // 동
    @Column(nullable = false)
    private String neighborhood;

    public StoreAddress(int postNum, String city, String district, String neighborhood) {
        this.postNum = postNum;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
    }

    public void updateAddress(int postNum, String city, String district, String neighborhood) {
        this.postNum = postNum;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
    }




}
