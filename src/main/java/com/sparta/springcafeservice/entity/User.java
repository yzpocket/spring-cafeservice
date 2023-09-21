package com.sparta.springcafeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    // 사용자는 0, 사업자는 0이외의 값
    private int registNum;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private int point = 0; //고객이 기본으로 가진 포인트 지급

    @OneToOne(mappedBy = "user")
    private Store store;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email, UserRoleEnum role, int registNum) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.registNum = registNum;
    }


    public void setPoint(int point){
        this.point = point;
    }

}