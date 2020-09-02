package com.kk.linyuanbin.demo2.handler;

import com.kk.linyuanbin.demo2.entity.Responser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.Subject;
import java.util.Iterator;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public Responser authorizationExceptionHandler(AuthorizationException e){
        /*Iterator<Object> iterator = SecurityUtils.getSubject().getPrincipals().iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }*/
        Responser responser = new Responser().setMsg(String.valueOf(HttpStatus.UNAUTHORIZED.getReasonPhrase() + " from GlobalControllerExceptionHandler(无授权)")).setCode(HttpStatus.UNAUTHORIZED.value()).setResult(null);
        return responser;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthenticationException.class)
    public Responser authenticationExceptionHandler(AuthenticationException e){
        Responser responser = new Responser().setMsg(String.valueOf(HttpStatus.NOT_FOUND.getReasonPhrase() + " from GlobalControllerExceptionHandler(未认证)")).setCode(HttpStatus.NOT_FOUND.value()).setResult(null);
        return responser;
    }
}
