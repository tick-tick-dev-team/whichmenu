package com.ticktick.whichmenu_backend.web.atch.service;

import java.util.List;

import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;

/**
 * 첨부파일 Service
 * */

public interface AtchFileService {

	// C
	void insertFileMeta(AtchFileDto dto);
	
	// R
	List<AtchFileDto> getFilesByReferId(int atchReferId);
	
	// U, atchUseYn = 'N'
	void updateFileMeta(AtchFileDto dto);
	
	// 참조 첨부파일 조회 - 단건 
	List<AtchFileDto> findFilesByReferId(AtchFileDto dto);
	
}
