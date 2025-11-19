package com.ticktick.whichmenu_backend.web.rgn.service;

import java.util.Map;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
	
	private final OAuthTokenDAO oAuthTokenDAO;

	/**
	 * 간편로그인 분기 처리
	 * */
	@Override
	public UsrInfoDto getUserInfo(Map<String, String> request, HttpSession session) {
		
		
		String provider = request.get("provider");
		if (provider == null || provider.isBlank()) {
			// naver, kakao 구분자 값이 없을때 튕구기
			throw new IllegalArgumentException("provider 값이 존재하지 않습니다.");
		}
		
		UsrInfoDto result = new UsrInfoDto();
		
		if ("naver".equalsIgnoreCase(provider)) {
			result =  getNaverUserInfo(request, session);
		} else if ("kakao".equalsIgnoreCase(provider)) {
			result =  getKakaoUserInfo(request, session);
		} else {
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
	@SuppressWarnings("unchecked")
	public UsrInfoDto getNaverUserInfo(Map<String, String> request, HttpSession session) {
		String accessToken = request.get("access_token");
		String refreshToken = request.get("refresh_token");
		
		long expiresInSec = 0L;
		
		try {
			expiresInSec = Long.parseLong(request.getOrDefault("expires_in", "0"));
		} catch (NumberFormatException e) {
			expiresInSec = 0L; // 예외 시 기본값
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
		naverUserInfo.put("prov", "naver");
		UsrInfoDto rgnUserInfo = findUser(naverUserInfo);
		
		// 2. 사용자 정보 + 토큰 저장
		OAuthToken savetoken = new OAuthToken();

		// 토큰 정보 저장 (DB)
		savetoken.setProvider("naver");
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
	 * 카카오 로그인 처리
	 * 
	 * 1) 사용자 조회 및 저장
	 * 2) 토큰 저장 및 세선 저장
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public UsrInfoDto getKakaoUserInfo(Map<String, String> request, HttpSession session) {
		String accessToken = request.get("access_token");
		String refreshToken = request.get("refresh_token");
		
		long expiresInSec = 0L;
		try {
			expiresInSec = Long.parseLong(request.getOrDefault("expires_in", "0"));
		} catch (NumberFormatException e) {
			expiresInSec = 0L; // 예외 시 기본값
		}
		
		// 1. 사용자 정보 요청
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		RestTemplate rest = new RestTemplate();
		ResponseEntity<Map> response = rest.exchange(
				"https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, Map.class
		);
		
		Map body = response.getBody();
		Map kakaoUserInfo = (Map) body.get("response");
		
		// 간편 로그인 정보 조회 및 추가
		kakaoUserInfo.put("prov", "kakao");
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
	public UsrInfoDto findUser(Map userInfo) {
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

}
