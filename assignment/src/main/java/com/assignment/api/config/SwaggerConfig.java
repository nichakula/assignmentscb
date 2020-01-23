package com.assignment.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

  
/**
 *
 * @author ton
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
          
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.assignment.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .enable(true)
            
        .securitySchemes(Lists.newArrayList(apiKey()))
        .securityContexts(Arrays.asList(securityContext()));
    }

    @Bean
    public SecurityConfiguration security() {
      return SecurityConfigurationBuilder.builder().scopeSeparator(",")
        .additionalQueryStringParams(null)
        .useBasicAuthenticationWithAccessCodeGrant(false).build();
    }


    private List<ApiKey> apiKey() {
        List<ApiKey> aKeys = new ArrayList<ApiKey>();
        ApiKey a1 = new ApiKey("apiKey", "X-API-KEY", "header");
        aKeys.add(a1);
        ApiKey a2 = new ApiKey("channelId", "X-CH-ID", "header");
        aKeys.add(a2);
        return aKeys;
    }

    private SecurityContext securityContext() {
      return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
      AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
      AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
      authorizationScopes[0] = authorizationScope;
      List<SecurityReference> sList = new ArrayList<SecurityReference>();
      SecurityReference s1 = new SecurityReference("apiKey", authorizationScopes);
      sList.add(s1);
      SecurityReference s2 = new SecurityReference("channelId", authorizationScopes);
      sList.add(s2);
      return sList;
    }

}
