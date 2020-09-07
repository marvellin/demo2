package com.kk.linyuanbin.demo2.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
//    @Autowired
//    @Resource(name = "stringRedisTemplate")
    private static RedisTemplate<String, String> redis;

//    @Autowired
//    @Resource
    private static StringRedisTemplate stringRedisTemplate;

    public Subject createSubject(SubjectContext context){
        /*//禁用session
        context.setSessionCreationEnabled(false);
        System.out.println("createSubject done");
        System.out.println("is WebSubjectContext?" + (context instanceof WebSubjectContext));
        Subject result = super.createSubject(context);
        System.out.println("is admin?" + result.hasRole("admin"));
        System.out.println("is user?" + result.hasRole("user"));
        System.out.println("is null?" + (result == null));
//        System.out.println("has session?" + (result.getSession() == null));
        System.out.println("is principle null?" + (result.getPrincipal() == null));
        System.out.println(result.isAuthenticated());
        return result;*/
//        initRedis();

        Subject result;

        if (!(context instanceof WebSubjectContext)) {
            return super.createSubject(context);
        }
        WebSubjectContext wsc = (WebSubjectContext) context;

        ServletRequest request = wsc.resolveServletRequest();
        ServletResponse response = wsc.resolveServletResponse();

        Token token = getToken((HttpServletRequest)request);
        System.out.println("token from StatelessDefaultSubjectFactory" + token.getToken());

        if (token == null || token.getToken() == null || token.getToken() == "") return result = super.createSubject(context);

        SecurityManager securityManager = wsc.resolveSecurityManager();
        context.setSessionCreationEnabled(false);

        //需要重写，以使用自定义方法获取
        /*PrincipalCollection principals = wsc.resolvePrincipals();
        boolean authenticated = wsc.resolveAuthenticated();*/
        PrincipalCollection principals = resolvePrincipals(token);
        boolean authenticated = myResolveAuthenticated(token);

        String host = wsc.resolveHost();
        Session session = wsc.resolveSession();
        boolean sessionEnabled = wsc.isSessionCreationEnabled();

        result = new WebDelegatingSubject(principals, authenticated, host, session, sessionEnabled,
                request, response, securityManager);
        return result;
    }

    public Token getToken(HttpServletRequest request){
        return new Token(request.getHeader("token"));
    }

    public PrincipalCollection resolvePrincipals(Token token){
        if (token == null || token.getToken() == null || token.getToken() == "") return null;
        PrincipalCollection collection = new SimplePrincipalCollection();
        collection.asSet().add(redis.opsForValue().get("random.token." + token.getToken()));
        return collection;
    }

    public boolean myResolveAuthenticated(Token token){
        if (null == token || token.getToken() == null || token.getToken() == "") return false;
        return redis.opsForValue().get(token.getToken()) == null ? false : true;
    }

    private void initRedis(){
        if (redis == null || stringRedisTemplate == null){
            RedisTemplate<String, String> template = new RedisTemplate<>();
            RedisConnectionFactory connectionFactory = new JedisConnectionFactory();
            template.setConnectionFactory(connectionFactory);
            JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
            template.setValueSerializer(serializer);
            template.setKeySerializer(new StringRedisSerializer());
            template.afterPropertiesSet();
            redis = template;

            StringRedisTemplate stringRedisTemplate1 = new StringRedisTemplate();
            stringRedisTemplate1.setConnectionFactory(connectionFactory);
            stringRedisTemplate = stringRedisTemplate1;
        }
    }
}
