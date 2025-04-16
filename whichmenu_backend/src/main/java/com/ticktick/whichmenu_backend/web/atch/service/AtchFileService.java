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
}
