package com.kk.linyuanbin.demo2.control;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "swagger test")
@RequestMapping("/swagger")
public class SwaggerController {

    @PostMapping("/")
    @ApiOperation("post test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param1", value = "参数1", defaultValue = "lin"),
            @ApiImplicitParam(name = "param2", value = "参数2", defaultValue = "yuanbin")
    })
    public void postTest(String param1, String param2){
        System.out.println(param1 + param2);
    }

    @GetMapping("/")
    @ApiOperation("get test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param1", value = "参数1", defaultValue = "lin"),
            @ApiImplicitParam(name = "param2", value = "参数2", defaultValue = "yuanbin")
    })
    public void getTest(String param1, String param2){
        System.out.println(param2 + param1);
    }
}
