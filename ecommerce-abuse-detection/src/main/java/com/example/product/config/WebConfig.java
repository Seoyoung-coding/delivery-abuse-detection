package com.example.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        // /uploads/** 주소로 요청이 오면
        registry.addResourceHandler("/uploads/**")

                // 실제 uploads 폴더에서 파일을 찾음
                .addResourceLocations("file:uploads/");
    }
}