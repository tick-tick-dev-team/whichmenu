package com.ticktick.whichmenu_backend.web.atch.dao.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 첨부파일 Dto
 * 
 * */

@Data
@Getter
@Setter
public class AtchFileDto {
	
	private int atchFileId;
	private int atchReferId;
	private int fileOdr;
	private String fileNm;
	private String filePath;
	private int fileSz;
	private String fileExtn;
	private String RefType;
	

}
