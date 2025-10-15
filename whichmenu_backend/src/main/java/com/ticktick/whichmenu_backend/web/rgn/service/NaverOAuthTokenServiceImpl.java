package com.ticktick.whichmenu_backend.web.rgn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ticktick.whichmenu_backend.web.rgn.dao.NaverLoginDAO;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.User;

public class NaverOAuthTokenServiceImpl implements NaverOAuthTokenService {
	
	@Autowired
	private NaverLoginDAO naverLoginDAO;

	@Override
	public OAuthToken findByProviderAndProviderUserId(String provider, String providerUserId) {
		return null;
	}

	@Override
	@Transactional
	public User handleNaverLogin(OAuthToken token, User userInfo) {
		// 사용자 존재 여부 확인
		User existingUser = naverLoginDAO.findByUsername(userInfo.getUsername());
		if (existingUser == null) {
			naverLoginDAO.insertUser(userInfo);
			existingUser = userInfo;
		}
		
		// 토큰 존재 여부 확인 후 upsert
		OAuthToken existingToken = naverLoginDAO.findTokenByProviderUserId(token.getProviderUserId());
		if (existingToken == null) {
			naverLoginDAO.insertOauthToken(token);
		} else {
			naverLoginDAO.updateOauthToken(token);
		}
		
		return existingUser;
	}

}
