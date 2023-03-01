package com.sy.keyword.common.exception;

public class BizException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    BizException(){
        super();
    }
    public BizException(String message){
        super(message);
    }
}
