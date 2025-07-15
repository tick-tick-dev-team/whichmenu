 package com.ticktick.whichmenu_backend.web.rest.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ticktick.whichmenu_backend.web.rest.dao.dto.MlMenuDto;

@Mapper
public interface MlMenuDAO {
	
	MlMenuDto selectOneRestMlMenu(MlMenuDto mlMenuDto);
	
	List<MlMenuDto> selectListRestMlMenu (MlMenuDto mlMenuDto);
	
	void insertMlMenu(MlMenuDto mlMenuDto);
	
	void updateMlMenu(MlMenuDto mlMenuDto);
	
	void deleteMlMenu(MlMenuDto mlMenuDto);

}
