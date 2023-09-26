package com.sparta.springcafeservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // [3] AOP 포인트컷 정의 - 실행 위치

    // Controller의 메소드 호출 시 로그 출력
    @Pointcut("execution(* com.sparta.springcafeservice.controller..*(..))")
    private void controllerMethods() {
    }

    // Service의 메소드 호출 시 로그 출력
    @Pointcut("execution(* com.sparta.springcafeservice.service..*(..))")
    private void serviceMethods() {
    }

    // Repository의 메소드 호출 시 로그 출력
    @Pointcut("execution(* com.sparta.springcafeservice.repository..*(..))")
    private void repositoryMethods() {
    }

    // Controller, Service, Repository 모두 포함하는 메소드 호출 시 로그 출력
    @Before("controllerMethods() || serviceMethods() || repositoryMethods()")
    public void logAllMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("3layer 중 메소드 '{}' 호출", methodName);
    }

    //
    //
    //// Controller의 메소드 반환 후 로그 출력
    //@AfterReturning(
    //        pointcut = "controllerMethods()",
    //        returning = "result"
    //)
    //
    //public void logControllerMethodResult(JoinPoint joinPoint, Object result) {
    //    String methodName = joinPoint.getSignature().getName();
    //    logger.info("Controller 메소드 '{}' 호출 -> 반환 : {}", methodName, result.toString());
    //}
    //
    //
    //
    //// Service의 메소드 반환 후 로그 출력
    //@AfterReturning(
    //        pointcut = "serviceCreateMethods()",
    //        returning = "result"
    //)
    //public void logServiceMethodResult(JoinPoint joinPoint, Object result) {
    //    String methodName = joinPoint.getSignature().getName();
    //    logger.info("Service 메소드 '{}' 호출 -> 반환 : {}", methodName, result.toString());
    //}
    //
    //
    //
    //// Repository의 메소드 반환 후 로그 출력
    //@AfterReturning(
    //        pointcut = "repositoryMethods()",
    //        returning = "result"
    //)
    //public void logRepositoryMethodResult(JoinPoint joinPoint, Object result) {
    //    String methodName = joinPoint.getSignature().getName();
    //    logger.info("Repository 메소드 '{}' 호출 -> 반환 : {}", methodName, result.toString());
    //}
}
