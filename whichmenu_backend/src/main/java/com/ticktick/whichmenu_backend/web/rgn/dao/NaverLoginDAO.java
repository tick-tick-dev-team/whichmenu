 package com.ticktick.whichmenu_backend.web.rgn.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ticktick.whichmenu_backend.web.rgn.dao.dto.OAuthToken;
import com.ticktick.whichmenu_backend.web.rgn.dao.dto.UsrInfoDto;

@Mapper
public interface NaverLoginDAO {
	
	UsrInfoDto findByUsername(String username);
	
	int insertUser(UsrInfoDto user);
	
	UsrInfoDto findByUsrInfo(UsrInfoDto usrInfoDto);
	
    // 토큰 조회
	OAuthToken findTokenByProviderUserId(String providerUserId);
	

    // 토큰 삽입
    void insertOauthToken(OAuthToken token);

    // 토큰 갱신
    void updateOauthToken(OAuthToken token);

}
