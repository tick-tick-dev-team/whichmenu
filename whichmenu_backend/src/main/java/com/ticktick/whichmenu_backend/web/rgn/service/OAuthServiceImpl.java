package com.ticktick.whichmenu_backend.web.rgn.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ticktick.whichmenu_backend.web.rgn.dao.OAuthTokenDAO;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

import jakarta.servlet.http.HttpSession;

@Service("oAuthServiceImpl")
public class OAuthServiceImpl implements OAuthService {
	
	private static final Logger log = LoggerFactory.getLogger(OAuthServiceImpl.class);
	
	private OAuthTokenDAO oAuthTokenDAO;
	
	private RestTemplate restTemplate;
	
	public OAuthServiceImpl(OAuthTokenDAO oAuthTokenDAO, RestTemplate restTemplate) {
		this.oAuthTokenDAO = oAuthTokenDAO;
		this.restTemplate  = restTemplate;
	}
	
	@Value("${naver.client.id}")
	private String NAVER_CLIENT_ID;
	
	@Value("${naver.client.secret}")
	private String NAVER_CLIENT_SECRET;
	
	@Value("${kakao.client.id}")
	private String KAKAO_CLIENT_ID;

	/**
	 * 간편로그인 분기 처리
	 * */
	@Override
	public UsrInfoDto getUserInfo(Map<String, String> request, HttpSession session) {
		
		
		String provider = request.get("provider");
		if (provider == null || provider.isBlank()) {
			// naver, kakao 구분자 값이 없을때 튕구기
			log.error("::::: [ Login processing ] provider 값이 올바르지 않습니다. provider => {} :::::", provider);
			throw new IllegalArgumentException("provider 값이 존재하지 않습니다.");
		}
		
		UsrInfoDto result = new UsrInfoDto();
		
		if ("naver".equalsIgnoreCase(provider)) {
			result =  getNaverUserInfo(request, session);
		} else if ("kakao".equalsIgnoreCase(provider)) {
			result =  getKakaoUserInfo(request, session);
		} else {
			log.error("::::: [ Login processing ] 지원되지 않는 provider => {} :::::", provider);
			throw new IllegalArgumentException("지원되지 않는 provider입니다.");
		}
		return result;
	}

	/**
	 * 네이버 로그인 처리
	 * 
	 * 1) 사용자 조회 및 저장
	 * 2) 토큰 저장 및 세선 저장
	 * 
	 * */
	@SuppressWarnings({ "unchecked" })
	public UsrInfoDto getNaverUserInfo(Map<String, String> request, HttpSession session) {
		
		String code  = request.get("code");
		String state = request.get("state");
		
		if(code == null || "".equals(code) || state == null || "".equals(state)) {
			log.error("::::: [ Login processing ] 간편로그인 처리 오류 code => {}, state => {}:::::",  code , state);
			throw new IllegalArgumentException("code 또는 state가 누락되었습니다.");
		}
		
		// 0. 접근 토큰 요청
		String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code" +
				"&client_id="		+ NAVER_CLIENT_ID +
				"&client_secret="	+ NAVER_CLIENT_SECRET +
				"&code="			+ code +
				"&state="			+ state;
		
		ResponseEntity<Map> tokenResponse = restTemplate.getForEntity(tokenUrl, Map.class);
		Map<String, Object> tokenMap = tokenResponse.getBody();

		// 접근 토큰 요청 결과값 체크
		if (tokenMap == null || tokenMap.isEmpty()) {
			log.error("::::: [ Login processing ] 네이버 간편 로그인 토큰 응답 실패 ::::: 요청정보 => {} ",  tokenUrl);
			throw new IllegalStateException("네이버 로그인 토큰 응답이 비어있습니다.");
		} else if (tokenMap.containsKey("error")) {
			log.error("::::: [ Login processing ] 네이버 간편 로그인 토큰 발급 실패 ::::: 요청정보 => {} ",  tokenUrl);
			log.error("::::: [ Login processing ] 네이버 간편 로그인 토큰 발급 실패 ::::: error 코드 => {}, error msg => {}", String.valueOf(tokenMap.get("error")) , String.valueOf(tokenMap.get("error_description")));
			throw new IllegalStateException("네이버 로그인 토큰 발급 실패했습니다.");
		}
		
		String accessToken  = (String) tokenMap.get("access_token");
		String refreshToken = (String) tokenMap.get("refresh_token");
		if(accessToken == null || accessToken.trim().isEmpty()) {
			log.error("::::: [ Login processing ] 네이버 간편 로그인 접근 토큰 오류 ::::: accessToken => {} ",  accessToken);
			throw new IllegalStateException("네이버 로그인 접근 토큰이 비어있습니다.");
		} else if(refreshToken == null || refreshToken.trim().isEmpty()) {
			log.error("::::: [ Login processing ] 네이버 간편 로그인 리프레쉬 토큰 오류 ::::: refreshToken => {} ",  refreshToken);
			throw new IllegalStateException("네이버 로그인 리프레쉬 토큰이 비어있습니다.");
		}
		long expiresInSec   = Long.parseLong(String.valueOf(tokenMap.get("expires_in")));
		
		// 1. 사용자 정보 요청
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		ResponseEntity<Map> response = restTemplate.exchange(
				"https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, Map.class
		);
		// 2. 사용자 정보 요청 결과값 체크
		Map<String, Object> body = (Map<String, Object>) response.getBody();
		if (body  == null || body .isEmpty()) {
			log.error("::::: [ Login processing ] 네이버 사용자 정보 body null ::::: ");
			throw new IllegalStateException("네이버 로그인 사용자 정보 응답이 비어있습니다.");
		} else if(!"00".equals(body.get("resultcode"))) {
			log.error("::::: [ Login processing ] 네이버 간편 로그인 사용자 정보 응답 실패 ::::: resultcode => {} , message => {}", String.valueOf(body.get("resultcode")), String.valueOf(body.get("message")));
			throw new IllegalStateException("네이버 로그인 사용자 정보 요청이 실패했습니다.");
		} 
		
		Map<String, Object> naverUserInfo = (Map<String, Object>) body.get("response");
		if(naverUserInfo == null || naverUserInfo.isEmpty()) {
			log.error("::::: [ Login processing ] 네이버 간편 로그인 사용자 response 없음 :::::");
			throw new IllegalStateException("네이버 사용자 정보가 없습니다.");
		}
		
		// 3. 간편 로그인 정보 조회 및 추가
		naverUserInfo.put("prov", "naver");
		UsrInfoDto rgnUserInfo = findUser(naverUserInfo);
		
		// 4. 사용자 정보 + 토큰 저장
		OAuthToken savetoken = new OAuthToken();

		// 토큰 정보 저장 (DB)
		savetoken.setProvider("naver");
		savetoken.setProviderUserId(rgnUserInfo.getUsrSn());
		savetoken.setAccessToken(accessToken);
		savetoken.setRefreshToken(refreshToken);
		savetoken.setExpiresAt(System.currentTimeMillis() + expiresInSec * 1000L);
		saveToken(savetoken);
		
		// 5. 세션 저장
		session.setAttribute("loginUser", rgnUserInfo);
		
		return rgnUserInfo;
	}
	
	/**
	 * 카카오 로그인 처리
	 * 
	 * 1) 사용자 조회 및 저장
	 * 2) 토큰 저장 및 세선 저장
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public UsrInfoDto getKakaoUserInfo(Map<String, String> request, HttpSession session) {
		
		String code = request.get("code");
		String redirectUri = request.get("redirect_uri");
		
		if(code == null || redirectUri == null) {
			log.error("::::: [ Login processing ] 간편로그인 처리 오류 code => {}, redirectUri => {}:::::",  code , redirectUri);
			throw new IllegalArgumentException("code 또는 redirectUri 정보가 누락되었습니다.");
		}
		
		String tokenUrl = "https://kauth.kakao.com/oauth/token" +
				"?grant_type=authorization_code" +
				"&client_id=" + KAKAO_CLIENT_ID  +
				"&redirect_uri=" + redirectUri +
				"&code=" + code;
		
		ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, null, Map.class);
		// + response 값 체크 로직 추가 필요
		
		Map<String, Object> tokenMap = tokenResponse.getBody();
		String accessToken = (String) tokenMap.get("access_token");
		String refreshToken = (String) tokenMap.get("refresh_token");
		long expiresInSec = Long.parseLong(String.valueOf(tokenMap.get("expires_in")));
		
		// 1. 사용자 정보 요청
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		ResponseEntity<Map> response = restTemplate.exchange(
				"https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, Map.class
		);
		
		Map<String, Object> body = (Map<String, Object>) response.getBody();
		Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		String email = kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : "";
		
		// 간편 로그인 정보 조회 및 추가		
		Map<String, Object> kakaoUserInfo = new HashMap<>();
		kakaoUserInfo.put("prov", "kakao");
		kakaoUserInfo.put("nickname", profile.get("nickname"));
		kakaoUserInfo.put("email", kakaoAccount.get("email") != null ? kakaoAccount.get("email") : "");
		UsrInfoDto rgnUserInfo = findUser(kakaoUserInfo);
		
		// 2. 사용자 정보 + 토큰 저장
		OAuthToken savetoken = new OAuthToken();

		// 토큰 정보 저장 (DB)
		savetoken.setProvider("kakao");
		savetoken.setProviderUserId(rgnUserInfo.getUsrSn());
		savetoken.setAccessToken(accessToken);
		savetoken.setRefreshToken(refreshToken);
		savetoken.setExpiresAt(System.currentTimeMillis() + expiresInSec * 1000L);
		saveToken(savetoken);
		
		// 2. 세션 저장
		session.setAttribute("loginUser", rgnUserInfo);

		return rgnUserInfo;
	}

	/**
	 * 사용자 조회 및 최초 저장
	 * 
	 * */
	public UsrInfoDto findUser(Map<String, Object> userInfo) {
		UsrInfoDto paramInfo = new UsrInfoDto();
		
		paramInfo.setProv(String.valueOf(userInfo.get("prov")));
		paramInfo.setNickNm(String.valueOf(userInfo.get("nickname")));
		paramInfo.setEmail(String.valueOf(userInfo.get("email")));
		
		UsrInfoDto findUsrInfo = oAuthTokenDAO.findByUsrInfo(paramInfo);
		
		if(findUsrInfo == null) {
			oAuthTokenDAO.insertUser(paramInfo);
			findUsrInfo = oAuthTokenDAO.findByUsrInfo(paramInfo);
		}
		
		return findUsrInfo;
	}

	/**
	 * 토큰 저장
	 * */
	public void saveToken(OAuthToken token) {
		oAuthTokenDAO.insertOauthToken(token);
	}

	/**
	 * 사용자 로그아웃 처리
	 * */
	@Override
	public Map<String, Object> logoutProc(HttpSession session) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 1. 사용자 아이디로 토큰 조회
		String provider = String.valueOf(session.getAttribute("provider"));
		String userId   = String.valueOf(session.getAttribute("userSn"));
		
		OAuthToken tokenInfo = oAuthTokenDAO.findTokenByProviderUserId(provider, userId);
		
		// 2. 토큰 삭제 처리
		if(tokenInfo != null) {
			oAuthTokenDAO.deleteToken(tokenInfo);
		} else {
			log.warn("::::: [ Logout processing ] DB에 토큰 정보 없음, 이미 삭제된 상태일 수 있음 ::::: tokenInfo => {}", tokenInfo);
		}
		
		// 3. 세션에 초기화
		session.invalidate(); 
		
		// 4. 로그아웃 처리 결과 리턴
		returnMap.put("status" , "success");
		returnMap.put("message", "로그아웃 처리 완료");
		
		return returnMap;
		
	}

}
