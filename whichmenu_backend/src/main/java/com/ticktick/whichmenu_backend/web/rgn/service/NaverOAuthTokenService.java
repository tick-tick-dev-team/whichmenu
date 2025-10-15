package com.ticktick.whichmenu_backend.web.rgn.service;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.User;

public interface NaverOAuthTokenService {
	
	OAuthToken findByProviderAndProviderUserId(String provider, String providerUserId);
	
	User handleNaverLogin(OAuthToken token, User userInfo);

}
