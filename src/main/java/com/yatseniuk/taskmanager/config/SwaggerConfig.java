package com.yatseniuk.taskmanager.config;

import com.google.common.collect.Lists;
import com.yatseniuk.taskmanager.constants.ConstantValues;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;

@Configuration
@EnableSwagger2

public class SwaggerConfig {

    @Bean
    public Docket api() {

//        Docket docket =
//                new Docket(DocumentationType.SWAGGER_2)
//                        .useDefaultResponseMessages(false)
//                        .pathMapping("/")
//                        .apiInfo(ApiInfo.DEFAULT)
//                        .forCodeGeneration(true)
//                        .genericModelSubstitutes(ResponseEntity.class)
//                        .ignoredParameterTypes(Pageable.class)
//                        .ignoredParameterTypes(java.sql.Date.class)
//                        .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
//                        .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
//                        .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
//                        .securityContexts(Lists.newArrayList(securityContext()))
//                        .securitySchemes(Lists.newArrayList(apiKey()))
//                        .useDefaultResponseMessages(false);

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .pathMapping("/")
                .apiInfo(ApiInfo.DEFAULT)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .ignoredParameterTypes(Pageable.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot REST API")
                .description("Task Management REST API")
                .contact(new Contact("Halyna Yatseniuk",
                        "http://localhost:8080/task-manager", "halina.yatseniuk@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", ConstantValues.AUTHORIZATION_HEADER.getValue(), "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(this::include)
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope =
                new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }

    private boolean include(String path) {
        return !path.startsWith("/ownSecurity");
    }
}