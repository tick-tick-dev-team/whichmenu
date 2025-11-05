package com.ticktick.whichmenu_backend.web.rgn.service;

import java.util.Map;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

public interface NaverOAuthTokenService {
	
	OAuthToken findByProviderAndProviderUserId(String provider, String providerUserId);
	
	UsrInfoDto handleNaverLogin(OAuthToken token, UsrInfoDto userInfo);
	
	UsrInfoDto findUser(Map userInfo);

}
