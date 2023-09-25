package com.sparta.springcafeservice.aop;// ResponseDtoLoggingAspect.java

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseDtoLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(ResponseDtoLoggingAspect.class);

    // Response DTO 클래스 내의 toString() 메서드 호출 시 로그 출력
    @Pointcut("execution(public String com.sparta.springcafeservice.dto..toString())")
    private void responseDtoToStringMethods() {
    }

    // Response DTO 클래스 내의 toString() 메서드 반환 후 로그 출력
    @AfterReturning(pointcut = "responseDtoToStringMethods()", returning = "result")
    public void logResponseDtoToStringMethodResult(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("{}.toString() result: {}", className, result);
    }
}