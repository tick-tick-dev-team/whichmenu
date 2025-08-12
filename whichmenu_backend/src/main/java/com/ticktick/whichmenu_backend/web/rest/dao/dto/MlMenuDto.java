package com.ticktick.whichmenu_backend.web.rest.dao.dto;

import java.util.List;

import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;

import lombok.Data;

/**
 * 식당별 식단 DTO
 * 
 * */
@Data
public class MlMenuDto {
	
	private String mlMenuId;		// 식사메뉴ID
	
	private String restId;			// 식당IDㄴ
	
	private String bgngDt;			// 게시시작일
	
	private String endDt;			// 게시종료일
	
	private String outsdReferUrl;	// 외부참조URL
	
	private String useYn;			// 사용여부
	
	private String regDt;			// 등록일
	
	private String mdfcnDt;			// 수정일
	
	private String restNm;			// 식당명
	
	private String restDtl;			// 식당상세내용
	
	private String infoInitType;	// 식당 정보개시유형
	
	private String restUseYn;		// 식당 사용 여부
	
	private AtchFileDto fileList;	// 첨부파일을 위한 변수
	
	private String srchDt;			// 조회조건날짜
	
	private String posType;		// 시점 조건
	
	private String fileChangedYn;
	
	

}
