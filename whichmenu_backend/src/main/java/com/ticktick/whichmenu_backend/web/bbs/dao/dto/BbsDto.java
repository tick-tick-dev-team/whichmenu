package com.ticktick.whichmenu_backend.web.bbs.dao.dto;

import java.util.List;

import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시판 DTO
 * 
 * */

@Data
@Getter
@Setter
public class BbsDto {
	
	private String bbsId;
	
	private String bbsType;
	
	private String bbsTtl;
	
	private String bbsCn;
	
	private String useYn;
	
	private String rlsYn;
	
	private String regNm;
	
	private String bbscttPw;
	
	private String regDt;
	
	private String mdfcnDt;
	
	private Integer atchReferId;
	
	/* 첨부파일을 위한 변수*/
	private List<AtchFileDto> fileList;
	

}
