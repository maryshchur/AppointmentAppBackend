package org.example.app.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.awt.print.Pageable;
import java.util.Date;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    @Bean
    public Docket swaggerSpringfoxDocket() {

        Docket docket =
                new Docket(DocumentationType.SWAGGER_2)
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
                        .securitySchemes(Lists.newArrayList(apiKey()))
                        .useDefaultResponseMessages(false);


        docket = docket.select().apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).build();
        return docket;

    }

}
