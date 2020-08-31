package com.kk.linyuanbin.demo2.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//需要认证的API被调用前执行的拦截器
public class TokenFilter extends AuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    private boolean login(String token){
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(new Token(token));
            return true;
        } catch (AuthenticationException e){
            return false;
        }
    }

    private String getToken(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //获取请求头中的Authorization属性
        String authorizationHeader = request.getHeader("token");
        if (!StringUtils.isEmpty(authorizationHeader)){
            return authorizationHeader.replace(" ", "");
        }
        return null;
    }

    private void printUnauthorized(String messageCode, HttpServletResponse response){
        throw new AuthorizationException();
    }
}
