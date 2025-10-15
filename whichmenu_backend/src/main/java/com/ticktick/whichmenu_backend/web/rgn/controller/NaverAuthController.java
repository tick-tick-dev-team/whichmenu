package com.ticktick.whichmenu_backend.web.rgn.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.User;
import com.ticktick.whichmenu_backend.web.rgn.service.NaverOAuthTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth/naver")
public class NaverAuthController {

	@Value("${naver.client.id}")
	private String clientId;
	
	@Value("${naver.client.secret}")
	private String clientSecret;
	
	@Autowired
	private NaverOAuthTokenService naverLoginService;
	
	@GetMapping("/callback")
	public void naverCallback(String access_token,
								String state,
								String token_type,
								Integer expires_in,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		
		// 1. 토큰 발급 요청
		String tokenUrl = "https://nid.naver.com/oauth2.0/token";
		RestTemplate restTemplate = new RestTemplate();
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
				.queryParam("grant_type", "authorization_code")
				.queryParam("client_id", clientId)
				.queryParam("client_secret", clientSecret)
				.queryParam("code", access_token)
				.queryParam("state", state);
	
		ResponseEntity<Map> tokenResponse = restTemplate.getForEntity(builder.toUriString(), Map.class);
		String accessToken = (String) tokenResponse.getBody().get("access_token");
		
		// 2. 사용자 정보 요청
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
				"https://openapi.naver.com/v1/nid/me",
				HttpMethod.GET,
				entity,
				Map.class
		);
	
		Map userInfo = (Map) ((Map) userInfoResponse.getBody().get("response"));
		
		// 3. 세션에 사용자 정보 저장
		request.getSession().setAttribute("loginUser", userInfo);
		
		// 4. 프론트 페이지로 리다이렉트
		response.sendRedirect("http://localhost:5173/ml/mlMain");
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> getLoginUser(HttpServletRequest request) {
		Object loginUser = request.getSession().getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
		}
		return ResponseEntity.ok(loginUser);
	}
	
	@PostMapping("/token")
	public User saveNaverToken(@RequestBody OAuthToken token, HttpSession session) {
		// 네이버 사용자 프로필 API를 통해 user 정보 가져왔다고 가정
		User user = new User();
		user.setUsername(token.getProviderUserId());
		user.setDisplayName("네이버사용자");
		user.setEmail("naver@example.com");
		
		User result = naverLoginService.handleNaverLogin(token, user);
		session.setAttribute("loginUser", result);
		
		return result;
	}
}

