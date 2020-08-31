package com.kk.linyuanbin.demo2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        //在请求头中添加一个为Authorization的属性，在shiro拦截器进行认证时使用
        Parameter parameter = new ParameterBuilder()
                .name("token")
                .description("token test")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kk.linyuanbin.demo2.control"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                    .title("SpringBoot整合Swagger")
                    .description("from linyuanbin")
                    .version("1.0")
                    .contact(new Contact("demo2", "http://www.qq.com", "1021478667@qq.com"))
                    .license("to baidu")
                    .licenseUrl("http://www.baidu.com")
                    .build());

    }
}
