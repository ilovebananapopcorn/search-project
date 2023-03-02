package com.sy.keyword.common.log;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class LogAspect {

    Logger agentLogger = LoggerFactory.getLogger("agentLog");

    @Around("execution(* com.sy.keyword.biz.search.controller.*.*(..))")
    public Object agentLogging(ProceedingJoinPoint pjp) throws Throwable{

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        long startTime = System.currentTimeMillis();
        agentLogger.info("∏{}∏{}∏{}∏","request",request.getRequestURI(), LocalDateTime.now());

        Object result = pjp.proceed();

        long endTime = System.currentTimeMillis();
        agentLogger.info("∏{}∏{}∏{}∏{}","response",request.getRequestURI(), LocalDateTime.now(), endTime-startTime);
        return result;
    }

}
