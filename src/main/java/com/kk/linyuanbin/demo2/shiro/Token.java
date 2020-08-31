package com.kk.linyuanbin.demo2.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class Token implements AuthenticationToken {
    public Token(String token) {

    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
