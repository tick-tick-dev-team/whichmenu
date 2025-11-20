package com.ticktick.whichmenu_backend.web.rgn.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;
import com.ticktick.whichmenu_backend.web.rgn.service.OAuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

	private final OAuthService oAuthService;
	
	private static final Logger log = LoggerFactory.getLogger(OAuthController.class);

	public OAuthController(@Qualifier("restInfoServiceImpl") OAuthService oAuthService) {
		this.oAuthService = oAuthService;
	}
	
	/**
	 * 사용자 로그인 처리
	 * */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpSession session) {
		try {
			UsrInfoDto userInfo = oAuthService.getUserInfo(request, session);
			return ResponseEntity.ok(userInfo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * 사용자 로그아웃 처리
	 * */
	@PostMapping("/logout")
	public Map<String, Object> logout(HttpSession session) {
		Map<String, Object> returnMap = oAuthService.logoutProc(session);
		return returnMap;
	}
}
