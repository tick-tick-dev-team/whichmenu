package com.ticktick.whichmenu_backend.web.rgn.dao.dto;

import lombok.Data;

@Data
public class UsrInfoDto {

	private String usrSn;	// 유저번호
	
	private String nickNm;	// 닉네임
	
	private String email;	// 이메일
	
	private String prov; 	// 로그인 구분값 (ex 'NAVER', 'KAKAO')
	
	private String usrRole;	// 사용자권한 ( U : 일반유저 , M : 관리자 )
	
	private String regDt;	// 등록일
	
	private String mdfcnDt;	// 수정일
}
