package com.yourssu.ssudateserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.annotation.AuthenticationPrincipal
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.OAS_30)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("com.yourssu.ssudateserver"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .ignoredParameterTypes(AuthenticationPrincipal::class.java)
    }

    fun securityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build()
    }

    fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOf(authorizationScope)
        return listOf(SecurityReference("Authorization", authorizationScopes))
    }

    fun apiKey(): ApiKey {
        return ApiKey("Authorization", "Bearer", "header")
    }

    fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder()
            .title("SSUDate")
            .description("Api Documentation")
            .version("1.0")
            .build()
    }
}
