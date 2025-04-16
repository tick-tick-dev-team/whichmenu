package com.ticktick.whichmenu_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/atch/**")  // 프론트에서 접근할 URL
                .addResourceLocations("file:///C:/whichmenuAtch/");  // 실제 파일 저장 위치
    }
}
