package com.ticktick.whichmenu_backend.web.rgn.service;

import java.util.Map;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

import jakarta.servlet.http.HttpSession;

/**
 * 토큰 Service
 * 
 * */

public interface OAuthService {
	
	// 로그인 사용자정보 조회
	UsrInfoDto getUserInfo(Map<String, String> request, HttpSession session);
	
	// 로그아웃 처리
	Map<String, Object> logoutProc(HttpSession session);
	
}
