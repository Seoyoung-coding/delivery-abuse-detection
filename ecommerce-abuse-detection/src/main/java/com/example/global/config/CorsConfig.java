package com.example.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")

                // Vue 개발 서버
                .allowedOrigins(
                        "http://localhost:5173"
                )

                // 허용할 HTTP Method
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )

                // Authorization JWT 포함
                .allowedHeaders("*")

                // preflight 결과 캐싱
                .maxAge(3600);
    }
}