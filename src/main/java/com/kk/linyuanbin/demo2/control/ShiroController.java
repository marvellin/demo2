package com.kk.linyuanbin.demo2.control;

import com.kk.linyuanbin.demo2.entity.Responser;
import com.kk.linyuanbin.demo2.shiro.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Random;

@RestController
@Api(tags = "shiro test")
@RequestMapping("/shiro")
public class ShiroController {
//    @Resource(name = "stringRedisTemplate")
    @Autowired
      private RedisTemplate<String, String> redis;
//    private ValueOperations<String, String> redis;

//    @Resource
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String[] tokens = {"token1", "token2", "token3"};

    private String[] roles = {"admin", "user"};

    @GetMapping("/getAdmin")
    @ApiOperation("shiro get admin test")
//    @ApiImplicitParam(name = "id", value = "标识符", dataType = "Integer", defaultValue = "1")
    @RequiresRoles(logical = Logical.OR, value = {"admin"})
    public Responser getAdmin(){
//        System.out.println(id);
        return new Responser().setCode(HttpStatus.OK.value()).setMsg("admin测试成功").setResult(null);
    }

    @GetMapping("/getUser")
    @ApiOperation("shiro get user test")
//    @ApiImplicitParam(name = "id", value = "标识符", dataType = "Integer", defaultValue = "1")
    @RequiresRoles(logical = Logical.OR, value = {"user"})
    public Responser getUser(){
//        System.out.println(id);
        return new Responser().setCode(HttpStatus.OK.value()).setMsg("user测试成功").setResult(null);
    }

    @PostMapping("/login")
    @ApiOperation("shiro login test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", defaultValue = "user"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "123456")
    })
    public Responser login(String name, String password){
        String token = "";
        String role = "";
        if(name.equals("user") && password.equals("123456")){
            Random random = new Random();
            //生产随机token
            token = tokens[random.nextInt(tokens.length)];
            //生成随机角色
            role = roles[random.nextInt(roles.length)];
            //将token和token对应的role存入redis
//            redis.set("random.token." + token, role);
            redis.opsForValue().set("random.token." + token, role);

            Subject currentUser = SecurityUtils.getSubject();

            try{
                currentUser.login(new Token(token));
                System.out.println("login success");
                Iterator<Object> iterator = currentUser.getPrincipals().iterator();
                while (iterator.hasNext()){
                    System.out.println(iterator.next().toString());
                }
            } catch (AuthenticationException e){
                return new Responser().setCode(HttpStatus.NOT_FOUND.value()).setMsg("登录失败").setResult(null);
            }

            return new Responser().setCode(HttpStatus.OK.value()).setMsg("登录成功").setResult(redis.opsForValue().get("random.token." + token));
        }
        return new Responser().setCode(HttpStatus.NOT_FOUND.value()).setMsg("登录失败").setResult(null);
    }
}
