package com.kk.linyuanbin.demo2.control;

import com.kk.linyuanbin.demo2.entity.Responser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Controller
@Api(tags = "test to catch authorizationException")
@RequestMapping("/authorizationException")
public class AuthorizationExceptionController {

    @GetMapping("/")
//    @RequestMapping("/")
    @ApiOperation("get test")
    @ApiImplicitParam(name = "id", value = "标识符", dataType = "Integer", defaultValue = "1")
    public Responser getTest(@RequestParam Integer id){
        throw new AuthorizationException();
    }
}
