package com.kk.linyuanbin.demo2.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

//自定义域对象 实现认证和授权的方法
public class TokenRealm extends AuthorizingRealm {

    public RedisTemplate<String, String> getRedis() {
        return redis;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    //    @Resource(name = "stringRedisTemplate")
    @Autowired
    private RedisTemplate<String, String> redis;
//    private ValueOperations<String, String> redis;

//    @Resource
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getName(){
        return "Realm";
    }

    @Override
    public boolean supports(AuthenticationToken token){
        System.out.println("supports done");
        return token != null && Token.class.isAssignableFrom(token.getClass());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权 Authorization");
        String role = (String) principalCollection.getPrimaryPrincipal();
        System.out.println(role + " from AuthorizationInfo");
        if (!StringUtils.isEmpty(role)){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole(role);
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("认证 Authentication");
        Token token = (Token) authenticationToken;
        //获取对应token角色
        String role = redis.opsForValue().get("random.token." + token.getToken());
//        String role = redis.get("random.token." + token.getToken());
        System.out.println(role + " from AuthenticationInfo");
        if (!StringUtils.isEmpty(role)){
            return new SimpleAuthenticationInfo(role, token.getToken(), this.getName());
        }
        return null;
    }
}
