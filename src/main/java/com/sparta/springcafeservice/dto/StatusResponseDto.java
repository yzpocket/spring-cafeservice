package com.sparta.springcafeservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class StatusResponseDto {

    private String msg;
    private int statuscode;

    public StatusResponseDto(String msg, int statuscode) {
        this.msg = msg;
        this.statuscode = statuscode;
    }
}
