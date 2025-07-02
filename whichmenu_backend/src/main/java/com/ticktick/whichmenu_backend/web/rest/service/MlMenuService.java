package com.ticktick.whichmenu_backend.web.rest.service;

import com.ticktick.whichmenu_backend.web.rest.dao.dto.MlMenuDto;

/**
 * 식단 Service
 * 
 * */

public interface MlMenuService {
	
	public MlMenuDto selectOneRestMenu(MlMenuDto mlMenuDto);
	
	public void insertMlMenu(MlMenuDto mlMenuDto);
	
	public void updateMlMenu(MlMenuDto mlMenuDto);
	
	public void deleteMlMenu(MlMenuDto mlMenuDto);

}
