package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.SignupRequestDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.entity.UserRoleEnum;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "${ADMIN_TOKEN}";

    public ResponseEntity<StatusResponseDto> signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        Optional<User> checkEmail = userRepository.findByEmail(requestDto.getEmail());


        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new RestApiException("중복된 사용자가 존재합니다.", HttpStatus.NOT_FOUND.value());
        }

        // email 중복확인
        String email = requestDto.getEmail();
        if (checkEmail.isPresent()) {
            throw new RestApiException("중복된 이메일이 존재합니다.", HttpStatus.NOT_FOUND.value());
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new RestApiException("관리자 암호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value());
            }
            role = UserRoleEnum.ADMIN;
        }

        StatusResponseDto res = new StatusResponseDto("회원가입이 완료되었습니다.", 200);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }
}