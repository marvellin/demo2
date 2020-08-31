package com.kk.linyuanbin.demo2.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMapping = shiroFilter.getFilterChainDefinitionMap();
        setUrl(filterChainDefinitionMapping, "anon", AnonUrl());
        setUrl(filterChainDefinitionMapping, "method,auth", AuthUrl());

        Map<String, Filter> filterMap = new HashMap<>();
        //methodFilter tokenFilter 自定义拦截器
        filterMap.put("method", new MethodFilter());
        filterMap.put("auth", new TokenFilter());
        shiroFilter.setFilters(filterMap);
        return shiroFilter;
    }

    private void setUrl(Map<String, String> filterChainDefinitionMapping, String filterName, List<String> urlList){
        if (!urlList.isEmpty()){
            Iterator<String> iterator = urlList.iterator();
            while (iterator.hasNext()){
                String url = iterator.next();
                filterChainDefinitionMapping.put(url, filterName);
            }
        }
    }

    //获取可匿名（无认证）访问路劲
    private List<String> AnonUrl(){
        List<String> list = new ArrayList<>();
        list.add("/swagger*");
        list.add("/api/login");
        return list;
    }

    //获取（拦截）需认证访问路劲
    private List<String> AuthUrl(){
        List<String> list = new ArrayList<>();
        list.add("/api/**");
        return list;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(TokenRealm tokenRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        //设置域对象
        manager.setRealm(tokenRealm);
        DefaultSubjectDAO dao = (DefaultSubjectDAO) manager.getSubjectDAO();

        //禁用session
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = (DefaultSessionStorageEvaluator) dao.getSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);

        StatelessDefaultSubjectFactory statelessDefaultSubjectFactory = new StatelessDefaultSubjectFactory();
        manager.setSubjectFactory(statelessDefaultSubjectFactory);
        manager.setSessionManager(this.defaultSessionManager());

        SecurityUtils.setSecurityManager(manager);
        return manager;
    }

    @Bean("tokenRealm")
    public TokenRealm tokenRealm(){
        return new TokenRealm();
    }

    @Bean
    public DefaultSessionManager defaultSessionManager(){
        DefaultSessionManager manager = new DefaultSessionManager();
        //禁用session
        manager.setSessionValidationSchedulerEnabled(false);
        return manager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public AuthorizationAttributeSourceAdvisor advisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
