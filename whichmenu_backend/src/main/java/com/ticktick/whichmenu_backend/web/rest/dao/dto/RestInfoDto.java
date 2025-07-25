package com.ticktick.whichmenu_backend.web.rest.dao.dto;

import lombok.Data;

/**
 * 식당 정보 DTO
 * 
 * */
@Data
public class RestInfoDto {
	
	private String restId;
	
	private String restNm;
	
	private String restDtl;
	
	private String restAddr;
	
	private String useYn;
	
	private String infoInitType; //정보 개시 유형 : DAY, WEEK
	
	private String regDt;
	
	private String mdfcnDt;
	
	private String srchKwrd; 	// 검색 키워드
	

}
