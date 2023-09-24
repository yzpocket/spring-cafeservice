package com.sparta.springcafeservice.aop;

import com.sparta.springcafeservice.dto.StatusResponseDto;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Aspect // [1] 관점(Aspect) 정의 부분 - 이 클래스가 Aspect클래스임을 알림
@Component // [2] 스프링 빈으로 등록 - IoC컨테이너에 Bean으로 등록
public class ServiceExceptionHandlerAspect {

    // [3] AOP 포인트컷 정의 - 실행 위치
    @Pointcut("execution(* com.sparta.springcafeservice..*Service.*(..))") // com.sparta.springcafeservice내 하위 *Service라는 파일들은 모두 이 AOP가 적용
    public void targetAllServiceMethods(){
    }

    // [4] AOP 어드바이스 정의 - 포인트컷에서 실제 동작하는 코드(구현체)
    /*
    Around=메서드 호출 전/후/예외 발생 시점
    Before=메서드 호출 전
    After=메서드 호출 후
    AfterReturning - 정상적 반환 이후
    AfterThrowing - 예외 발생 이후
    */

    // [4-1] Service 클래스에서 반환을 통일시키는 부가기능
    @Around("targetAllServiceMethods()")
    public ResponseEntity<StatusResponseDto> handleServiceRequest(Supplier<StatusResponseDto> action) {
        try {
            return new ResponseEntity<>(action.get(), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new StatusResponseDto(ex.getMessage(), 400), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new StatusResponseDto("서비스 요청 중 오류가 발생했습니다.", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}