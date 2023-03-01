package com.sy.keyword.common.web.handler;

import com.sy.keyword.biz.search.vo.response.ErrorResponse;
import com.sy.keyword.common.exception.BizException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.builder().errCode("E01").errMsg(e.getMessage()).build();
        return response;
    }

    /**
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ErrorResponse handleBindException(BindException e) {
        final ErrorResponse response = ErrorResponse.builder().errCode("E02").errMsg(e.getMessage()).build();
        return response;
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        final ErrorResponse response = ErrorResponse.builder().errCode("E03").errMsg(e.getMessage()).build();
        return response;
    }

    @ExceptionHandler(BizException.class)
    protected ErrorResponse handleBizException(BizException e) {
        final ErrorResponse response = ErrorResponse.builder().errCode("E04").errMsg(e.getMessage()).build();
        return response;
    }
    @ExceptionHandler(NoSuchElementException.class)
    protected ErrorResponse handleException(NoSuchElementException e) {
        final ErrorResponse response = ErrorResponse.builder().errCode("E05").errMsg(e.getMessage()).build();
        return response;
    }

    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(Exception e) {
        final ErrorResponse response = ErrorResponse.builder().errCode("E99").errMsg(e.getMessage()).build();
        return response;
    }
}