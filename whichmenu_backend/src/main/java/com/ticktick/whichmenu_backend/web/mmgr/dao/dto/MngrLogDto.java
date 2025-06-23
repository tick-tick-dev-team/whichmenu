package com.ticktick.whichmenu_backend.web.mmgr.dao.dto;

import lombok.Data;

/**
 * 관리자 로그 DTO
 * 
 * */
@Data
public class MngrLogDto {
	
	String mngrNo;	// 관리자번호
	
	String mngrNm;	// 관리자명
	
	String lngDt;	// 로그인 일시
	
	String lngYn;	// 로그인성공여부
	
	String srchBgngDt;	// 검색시작일
	
	String srchEndDt;	// 검색종료일

}
