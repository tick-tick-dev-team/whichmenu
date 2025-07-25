package com.ticktick.whichmenu_backend.web.atch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;

@Mapper
public interface AtchFileDAO {

	// 파일 데이터 저장
	void insertFileMeta(AtchFileDto fileDto);
	
	// R
	List<AtchFileDto> selectFilesByReferId(@Param("atchReferId") int atchReferId);
	
	// U, atchUseYn
	void updateFileMeta(AtchFileDto fileDto);
	
	// 참조 파일 조회 (R : atchReferId, refType)
	List<AtchFileDto> selectFilesByReferIdAndType(AtchFileDto fileDto);
	
	
}
