package com.sparta.springcafeservice.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.springcafeservice.entity.StoreAddress;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class StoreRequestDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank(message = "패스워드를 입력하세요.")
    private String password;

    @NotBlank
    @Size(max = 15, message = "상점 이름은 15자를 초과할 수 없습니다.")
    private String storeName;

    @NotBlank
    @Size(min = 10, message = "상점 소개를 10자 이상 작성해주세요.")
    private String information;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 1000000000, message = "사업자 번호는 10자리여야 합니다.")
    @Max(value = 9999999999L, message = "사업자 번호는 10자리여야 합니다.")
    private int businessNum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 10000, message = "우편 번호는 5자리여야 합니다.")
    @Max(value = 99999, message = "우편 번호는 5자리여야 합니다.")
    private int postNum;


    @Size(max = 10, message = "도시명은 10자를 초과할 수 없습니다.")
    private String city;


    @Size(max = 10, message = "구/군 이름은 10자를 초과할 수 없습니다.")
    private String district;


    @Size(max = 10, message = "동/면 이름은 10자를 초과할 수 없습니다.")
    private String neighborhood;


    public StoreAddress toStoreAddress() {
        return new StoreAddress(postNum, city, district, neighborhood);
    }



}
