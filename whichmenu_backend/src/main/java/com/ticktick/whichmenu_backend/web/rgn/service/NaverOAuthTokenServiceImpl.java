package com.ticktick.whichmenu_backend.web.rgn.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticktick.whichmenu_backend.web.rest.dao.RestInfoDAO;
import com.ticktick.whichmenu_backend.web.rgn.dao.NaverLoginDAO;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

@Service
public class NaverOAuthTokenServiceImpl implements NaverOAuthTokenService {
	

	private NaverLoginDAO naverLoginDAO;
	
	public NaverOAuthTokenServiceImpl(NaverLoginDAO naverLoginDAO) {
		this.naverLoginDAO = naverLoginDAO;
	}

	@Override
	public OAuthToken findByProviderAndProviderUserId(String provider, String providerUserId) {
		return null;
	}

	@Override
	@Transactional
	public UsrInfoDto handleNaverLogin(OAuthToken token, UsrInfoDto userInfo) {
		// 사용자 존재 여부 확인
		UsrInfoDto existingUser = naverLoginDAO.findByUsername(userInfo.getNickNm());
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

	@Override
	public UsrInfoDto findUser(Map userInfo) {
		
		UsrInfoDto paramInfo = new UsrInfoDto();
		
		paramInfo.setProv("NAVER");
		paramInfo.setNickNm(String.valueOf(userInfo.get("nickname")));
		paramInfo.setEmail(String.valueOf(userInfo.get("email")));
		
		UsrInfoDto findUsrInfo = naverLoginDAO.findByUsrInfo(paramInfo);
		
		if(findUsrInfo == null) {
			naverLoginDAO.insertUser(findUsrInfo);
			findUsrInfo = naverLoginDAO.findByUsrInfo(paramInfo);
		}
		
		return findUsrInfo;
	}



}
