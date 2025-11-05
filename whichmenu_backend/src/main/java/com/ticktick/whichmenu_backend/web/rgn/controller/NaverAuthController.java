package com.ticktick.whichmenu_backend.web.rgn.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ticktick.whichmenu_backend.web.rest.service.RestInfoService;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;
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
	
	private final NaverOAuthTokenService naverLoginService;
	
	public NaverAuthController(@Qualifier("naverOAuthTokenServiceImpl") NaverOAuthTokenService naverLoginService) {
		this.naverLoginService = naverLoginService;
	}
	
	@GetMapping("/callback")
	public void naverCallback(String access_token,
								String state,
								String token_type,
								Integer expires_in,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		
		// 1. 토큰 발급 요청
//		
//		Map userInfo = (Map) ((Map) userInfoResponse.getBody().get("response"));
		
		// 3. 세션에 사용자 정보 저장
//		request.getSession().setAttribute("loginUser", userInfo);
		
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
	public ResponseEntity<?> saveNaverToken(@RequestBody Map<String, String> request, HttpSession session) {
		String accessToken = request.get("access_token");
		
		if (accessToken == null || accessToken.isBlank()) {
			return ResponseEntity.badRequest().body("access_token이 누락되었습니다.");
		}
		
		// 1. 사용자 정보 요청
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		RestTemplate rest = new RestTemplate();
		ResponseEntity<Map> response = rest.exchange(
			"https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, Map.class
		);
		
		Map body = response.getBody();
		Map naverUserInfo = (Map) body.get("response");
		
		// 간편 로그인 정보 조회 및 추가
		UsrInfoDto rgnUserInfo = naverLoginService.findUser(naverUserInfo);
		
		// 2. 세션 저장
		session.setAttribute("loginUser", rgnUserInfo);
		 
		// 3. 응답 반환
		return ResponseEntity.ok(naverUserInfo);
	}
	
	@GetMapping("/session")
	public ResponseEntity<?> getSessionUser(HttpSession session) {
		Object loginUser = session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보 없음");
		}
		return ResponseEntity.ok(loginUser);
	}
}

