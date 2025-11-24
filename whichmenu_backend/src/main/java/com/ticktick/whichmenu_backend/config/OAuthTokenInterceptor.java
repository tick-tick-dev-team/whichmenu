package com.ticktick.whichmenu_backend.config;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ticktick.whichmenu_backend.web.rgn.dao.OAuthTokenDAO;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthTokenInterceptor implements HandlerInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(OAuthTokenInterceptor.class);
	
	@Value("${naver.client.id}")
	private String naverClientId;
	
	@Value("${naver.client.secret}")
	private String naverClientSecret;
	
	@Value("${kakao.client.id}")
	private String kakaoClientId;
	
	private final OAuthTokenDAO tokenDAO;
	
	private final RestTemplate restTemplate;
	
	// 1시간 체크(ms)
	private static final long CHECK_INTERVAL = 60 * 60 * 1000L;
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			log.info("::::: [OAuthTokenInterceptor] 세션 정보가 없습니다. :::::");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("로그인 세션이 없습니다.");
			return false;
		}
		UsrInfoDto loginUser = (UsrInfoDto) session.getAttribute("loginUser");
		if (loginUser == null) {
			log.info("::::: [OAuthTokenInterceptor] 로그인 사용자 정보가 없습니다. :::::");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("로그인 정보가 없습니다.");
			return false;
		}
		
		String provider = loginUser.getProv();;
		String userId = loginUser.getUsrSn();;
		
		if (   provider == null || "".equals(provider) || userId   == null || "".equals(userId)) {
			log.info("::::: [OAuthTokenInterceptor] 로그인 사용자 정보가 올바르지 않습니다. provider => {}, userId => {} :::::", provider, userId);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("로그인 정보가 올바르지 않습니다.");
			return false;
		}
		
		OAuthToken token = tokenDAO.findTokenByProviderUserId(provider, userId);
		if (token == null || "".equals(token)) {
			log.info("::::: [OAuthTokenInterceptor] 사용자 토큰 정보가 올바르지 않습니다. token => {} :::::", token);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("OAuth 토큰 정보가 없습니다.");
			return false;
		}
		
		//토큰 30분남았을때 체
		//long now = System.currentTimeMillis();
		//long threshold = 30 * 60 * 1000L; // 30분(ms)
		//if (token.getExpiresAt() - now <= threshold) {

		// 만료 여부 체크
		long now = System.currentTimeMillis();
		long lastCheckMillis = token.getUpdatedAt().toEpochMilli();
		
		// 마지막 체크 1시간 이후만 갱신 로직 실행
		if (now - lastCheckMillis >= CHECK_INTERVAL) {
			
			// access_token 만료 시 refresh_token으로 갱신
			if (now > token.getExpiresAt()) {
				
				if ("naver".equalsIgnoreCase(provider)) {
					String url = "https://nid.naver.com/oauth2.0/token"
							+ "?grant_type=refresh_token"
							+ "&client_id=" + naverClientId
							+ "&client_secret=" + naverClientSecret
							+ "&refresh_token=" + token.getRefreshToken();
					
					
					Map<String, Object> res = restTemplate.getForObject(url, Map.class);
					token.setAccessToken((String) res.get("access_token"));
					token.setExpiresAt(System.currentTimeMillis() + 3600 * 1000L);
					token.setUpdatedAt(Instant.now());
					tokenDAO.updateAccessToken(token);
					
				} else if ("kakao".equalsIgnoreCase(provider)) {
					String url = "https://kauth.kakao.com/oauth/token"
							+ "?grant_type=refresh_token"
							+ "&client_id=" + kakaoClientId
							+ "&refresh_token=" + token.getRefreshToken();
					
					Map<String, Object> res = restTemplate.postForObject(url, null, Map.class);
					token.setAccessToken((String) res.get("access_token"));
					token.setExpiresAt(System.currentTimeMillis() + 3600 * 1000L);
					token.setUpdatedAt(Instant.now());
					tokenDAO.updateAccessToken(token);
				}
			}
		}
		
		return true;
	}
}
