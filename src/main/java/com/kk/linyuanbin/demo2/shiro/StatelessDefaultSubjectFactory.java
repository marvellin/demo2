package com.kk.linyuanbin.demo2.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;

public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context){
        //禁用session
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
        return result;
    }
}
