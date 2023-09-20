package com.sparta.springcafeservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springcafeservice.dto.LoginRequestDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.UserRoleEnum;
import com.sparta.springcafeservice.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            // 요청 본문이 비어 있는지 확인
            if (request.getContentLength() == 0) {
                throw new RuntimeException("요청 본문이 비어 있습니다.");
            }

            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error("예외 발생", e);
            throw new RuntimeException("요청 처리 중 오류가 발생했습니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 JWT 생성");

        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getEmail(); // username -> email
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(email, role); // username -> email
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); // 헤더에 담기
//        jwtUtil.addJwtToCookie(token, response); // 쿠키에 담기

        StatusResponseDto responseDto = new StatusResponseDto("로그인 성공", 200);

        // 응답 데이터 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        // JSON 변환 후 출력
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");
        response.setStatus(401);

        String errorMessage = "로그인 실패";
        if (failed instanceof BadCredentialsException) {
            errorMessage = "잘못된 비밀번호입니다.";
        } else if (failed instanceof UsernameNotFoundException) {
            errorMessage = "해당 사용자를 찾을 수 없습니다.";
        }

        StatusResponseDto responseDto = new StatusResponseDto(errorMessage, 400);

        // 응답 데이터 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        // JSON 변환 후 출력
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseDto);
    }

}