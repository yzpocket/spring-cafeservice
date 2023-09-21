package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private String menuName;

    private String image;

    private int price;

    public MenuResponseDto(Menu menu) {
        this.menuName = menu.getMenuName();
        this.image = menu.getImagePath();
        this.price = menu.getPrice();
    }
}
