package com.ticktick.whichmenu_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Autowired
	private OAuthTokenInterceptor oAuthTokenInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/atch/**")							// 프론트에서 접근할 URL
				.addResourceLocations("file:///C:/whichmenuAtch/");		// 실제 파일 저장 위치
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration reg = registry.addInterceptor(oAuthTokenInterceptor);
		reg.addPathPatterns("/api/**"); 													// 갱신 필요한 API 경로
		reg.excludePathPatterns(new String[]{"/", "/ml/mlMain", "/public/**", "/login" });	// 로그인 정보 없이도 접근 가능 URL
	}
}
