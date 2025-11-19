package com.ticktick.whichmenu_backend.web.rgn.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;
import com.ticktick.whichmenu_backend.web.rgn.service.OAuthService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OAuthController {

	private final OAuthService oAuthService;

	@PostMapping("/api/oauth/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpSession session) {
		try {
			UsrInfoDto userInfo = oAuthService.getUserInfo(request, session);
			return ResponseEntity.ok(userInfo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
