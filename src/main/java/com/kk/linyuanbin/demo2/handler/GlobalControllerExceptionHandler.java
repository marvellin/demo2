package com.kk.linyuanbin.demo2.handler;

import com.kk.linyuanbin.demo2.entity.Responser;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public Responser authorizationExceptionHandler(AuthorizationException e){
        Responser responser = new Responser().setMsg(String.valueOf(HttpStatus.UNAUTHORIZED.getReasonPhrase())).setCode(HttpStatus.UNAUTHORIZED.value()).setResult(null);
        return responser;
    }
}
