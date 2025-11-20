package com.ticktick.whichmenu_backend.web.rgn.service;

import java.util.Map;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

import jakarta.servlet.http.HttpSession;

public interface OAuthService {
	
	UsrInfoDto getUserInfo(Map<String, String> request, HttpSession session);
	
	Map<String, Object> logoutProc(HttpSession session);
	
}
