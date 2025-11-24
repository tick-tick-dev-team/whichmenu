package com.ticktick.whichmenu_backend.web.rgn.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

@Mapper
public interface OAuthTokenDAO {
	
	// 유저 조회
	UsrInfoDto findByUsrInfo(UsrInfoDto usrInfoDto);
	
	// 유저 추가
	int insertUser(UsrInfoDto user);
	
	// 토큰 삽입
	void insertOauthToken(OAuthToken token);
	
	// 토큰 갱신
	int updateAccessToken(OAuthToken token);
	
	// 토큰 체크시 유저의 토큰 조회
	OAuthToken findTokenByProviderUserId(String provider, String userId);
	
	int deleteToken(OAuthToken token);

}
