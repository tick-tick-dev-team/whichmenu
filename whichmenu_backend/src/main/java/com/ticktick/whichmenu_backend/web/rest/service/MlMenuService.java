package com.ticktick.whichmenu_backend.web.rest.service;

import java.util.List;

import com.ticktick.whichmenu_backend.web.rest.dao.dto.MlMenuDto;

/**
 * 식단 Service
 * 
 * */

public interface MlMenuService {
	
	public MlMenuDto selectOneRestMenu(MlMenuDto mlMenuDto);
	
	public List<MlMenuDto> selectListRestMlMenu (MlMenuDto mlMenuDto);
	
	public int insertMlMenu(MlMenuDto mlMenuDto);
	
	public void updateMlMenu(MlMenuDto mlMenuDto);
	
	public void deleteMlMenu(MlMenuDto mlMenuDto);
	
	public boolean mlmenuOverlapCheck(MlMenuDto mlMenuDto);
	
	public String getMlmenuId(MlMenuDto mlMenuDto);

}
