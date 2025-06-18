package com.ssm.webdbtest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
// 正确写法（适用于 Spring Boot 3）
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class RequestLogAspect {

    @Before("execution(public * com.ssm.webdbtest.controller..*.*(..))")
    public void logRequest(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("Request URL: {}", request.getRequestURL());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("IP Address: {}", request.getRemoteAddr());
        log.info("Class Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("Arguments: {}", joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(public * com.ssm.webdbtest.controller..*.*(..))", returning = "result")
    public void logAfterReturning(Object result) {
        log.info("Response: {}", result);
    }

    @AfterThrowing(pointcut = "execution(public * com.ssm.webdbtest.controller..*.*(..))", throwing = "e")
    public void logAfterThrowing(Throwable e) {
        log.error("Exception: ", e);
    }
}
