package com.sparta.springcafeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteResponseDto {
    private String msg;
    private int statusCode;

    public DeleteResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
