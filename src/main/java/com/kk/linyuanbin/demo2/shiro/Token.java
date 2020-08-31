package com.kk.linyuanbin.demo2.shiro;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

//自定义token 实现Authenticationtoken 认证需要用到
@Data
@NoArgsConstructor
public class Token implements AuthenticationToken {
    private String token;

    @Override
    public Object getPrincipal() {
        return this.getToken();
    }

    @Override
    public Object getCredentials() {
        return this.getToken();
    }
}
