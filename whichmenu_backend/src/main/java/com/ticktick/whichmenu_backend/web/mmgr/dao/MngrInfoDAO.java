package com.ticktick.whichmenu_backend.web.mmgr.dao;

import java.util.List;

/**
 * 관리자 DAO
 * 
 * */

import org.apache.ibatis.annotations.Mapper;

import com.ticktick.whichmenu_backend.web.mmgr.dao.dto.MngrInfoDto;

@Mapper
public interface MngrInfoDAO {
	
	List<MngrInfoDto> selectAllMngrs();

}
