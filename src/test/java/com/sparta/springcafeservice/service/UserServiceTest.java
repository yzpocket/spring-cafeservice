package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.SignupRequestDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void testSignup_Success() throws NoSuchFieldException, IllegalAccessException {

        SignupRequestDto requestDto = new SignupRequestDto();

        Field usernameField = SignupRequestDto.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(requestDto, "testUser");

        Field passwordField = SignupRequestDto.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        passwordField.set(requestDto, "testPassword");

        Field emailField = SignupRequestDto.class.getDeclaredField("email");
        emailField.setAccessible(true);
        emailField.set(requestDto, "testEmail@test.com");

        Field adminFlag = SignupRequestDto.class.getDeclaredField("admin");
        adminFlag.setAccessible(true);
        adminFlag.set(requestDto, false);

        StatusResponseDto response =  userService.signup(requestDto).getBody();

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());


        assertEquals("회원가입이 완료되었습니다.", response.getMsg());
        assertEquals(200, response.getStatuscode());

    }


    @Test
    public void testSignup_DuplicateUsername_Failure() throws NoSuchFieldException, IllegalAccessException {
        // Prepare test data
        User existingUser = new User();
        Field usernameField = User.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(existingUser, "testUser");

        // findByUsername이 "testUser"와 함께 호출될 때 기존 사용자를 반환하도록 UserRepository 구성
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

        // 같은 이름 생성
        SignupRequestDto requestDto = new SignupRequestDto();

        Field requestUsername = requestDto.getClass().getDeclaredField("username");
        requestUsername.setAccessible(true);
        requestUsername.set(requestDto, "testUser");


        Exception exception = assertThrows(RestApiException.class,
                () -> userService.signup(requestDto));

        assertEquals("중복된 사용자가 존재합니다.", exception.getMessage());
    }

    @Test
    public void testSignup_DuplicateEmail_Failure() throws NoSuchFieldException, IllegalAccessException {
        // Prepare test data
        User existingUser = new User();
        Field emailField = User.class.getDeclaredField("email");
        emailField.setAccessible(true);
        emailField.set(existingUser, "testEmail@test.com");

        // findByEmail이 "testEmail@test.com "과 함께 호출될 때 기존 사용자를 반환하도록 UserRepository 구성
        when(userRepository.findByEmail("testEmail@test.com")).thenReturn(Optional.of(existingUser));

        // 같은 이메일 생성
        SignupRequestDto requestDto = new SignupRequestDto();

        Field requestEmail= requestDto.getClass().getDeclaredField("email");
        requestEmail.setAccessible(true);
        requestEmail.set(requestDto , "testEmail@test.com");

        Exception exception =  assertThrows(RestApiException.class,
                () -> userService.signup(requestDto));

        assertEquals("중복된 이메일이 존재합니다." , exception.getMessage());
    }


}