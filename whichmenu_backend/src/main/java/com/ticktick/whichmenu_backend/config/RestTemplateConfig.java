package com.ticktick.whichmenu_backend.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	
	@Bean
	RestTemplate restTemplate() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(50);						// 전체 커넥션 최대 개수
		connectionManager.setDefaultMaxPerRoute(20);			// 호스트(서버)별 최대 연결 수
		
		RequestConfig config = RequestConfig.custom()
			.setConnectionRequestTimeout(Timeout.ofSeconds(5))	// 커넥션 풀에서 가져오는 시간
			.setResponseTimeout(Timeout.ofSeconds(10)) 			// 서버 응답 최대 시간
			.build();
		
		CloseableHttpClient httpClient = HttpClients.custom()	// httpClient 5.x 버전에서 이렇게 사용하는게 안전함
			.setConnectionManager(connectionManager)			// 커넥션 풀
			.setDefaultRequestConfig(config)					// 타임아웃 설정
			.build();
		

		
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
	}

}
