package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.SignupRequestDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.entity.UserRoleEnum;
import com.sparta.springcafeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        String registNum = requestDto.getRegistNum();
        Optional<User> checkEmail = userRepository.findByEmail(requestDto.getEmail());


        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            StatusResponseDto res = new StatusResponseDto("중복된 사용자가 존재합니다.", 400);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        // email 중복확인
        String email = requestDto.getEmail();
        if (checkEmail.isPresent()) {
            StatusResponseDto res = new StatusResponseDto("중복된 이메일입니다.", 400);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                StatusResponseDto res = new StatusResponseDto("관리자 암호가 틀려 등록이 불가능합니다.", 400);
                return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
            }
            role = UserRoleEnum.ADMIN;
        }


        if (registNum == null) {
            // 사용자 등록 -> 백만 포인트 지급
            User user = new User(username, password, email, role);
            if (role.equals(UserRoleEnum.USER)) {
                user.setPoint(1000000);
                userRepository.save(user);

                StatusResponseDto res = new StatusResponseDto("회원가입이 완료되었습니다.", 200);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        } else {
            User user = new User(username, password, email, role, registNum);
            userRepository.save(user);

            StatusResponseDto res = new StatusResponseDto("사업자 회원가입이 완료되었습니다.", 200);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        return null;
    }

}