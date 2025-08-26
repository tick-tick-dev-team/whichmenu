package com.ticktick.whichmenu_backend.web.bbs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticktick.whichmenu_backend.web.bbs.dao.dto.BbsDto;

@Mapper
public interface BbsDAO {
	
	List<BbsDto> selectAllBbs(BbsDto bbsDto);
	
	BbsDto selectOneBbs(BbsDto bbsDto);
	
	BbsDto selectBbsWithFiles(BbsDto bbsDto);
	
	void insertBbs(BbsDto bbsDto);
	
	void updateBbs(BbsDto bbsDto);
	
	void deleteBbs(BbsDto bbsDto);

	int countExistingFilesByReferId(@Param("atchReferId") String atchReferId);
}
