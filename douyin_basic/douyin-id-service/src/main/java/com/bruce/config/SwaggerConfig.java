package com.bruce.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.*")) // 扫描的包路径
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()) // 所有路径
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("视频微服务 文档")
                        .description("项目接口文档")
                        .version("1.0")
                        .build());
    }
}
