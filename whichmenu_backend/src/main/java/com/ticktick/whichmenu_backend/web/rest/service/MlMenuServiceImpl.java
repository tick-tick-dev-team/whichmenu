package com.ticktick.whichmenu_backend.web.rest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.rest.dao.MlMenuDAO;
import com.ticktick.whichmenu_backend.web.rest.dao.dto.MlMenuDto;

/**
 * 식단 ServiceImpl
 * 
 * */
@Service
public class MlMenuServiceImpl implements MlMenuService {
	
	private static final Logger log = LoggerFactory.getLogger(MlMenuServiceImpl.class);

	private MlMenuDAO mlMenuDao;
	
	public MlMenuServiceImpl(MlMenuDAO mlMenuDao) {
		this.mlMenuDao = mlMenuDao;
	}

	@Override
	public MlMenuDto selectOneRestMenu(MlMenuDto mlMenuDto) {
		MlMenuDto rsltDto =  mlMenuDao.selectOneRestMlMenu(mlMenuDto);
		return rsltDto;
	}

	@Override
	public int insertMlMenu(MlMenuDto mlMenuDto) {
		int mlMenuId = mlMenuDao.insertMlMenu(mlMenuDto);
		return mlMenuId;
	}

	@Override
	public void updateMlMenu(MlMenuDto mlMenuDto) {
		mlMenuDao.updateMlMenu(mlMenuDto);
	}

	@Override
	public void deleteMlMenu(MlMenuDto mlMenuDto) {
		mlMenuDao.deleteMlMenu(mlMenuDto);
	}

	@Override
	public List<MlMenuDto> selectListRestMlMenu(MlMenuDto mlMenuDto) {
		List<MlMenuDto> rsltList = mlMenuDao.selectListRestMlMenu(mlMenuDto);
		return rsltList;
	}

	@Override
	public boolean mlmenuOverlapCheck(MlMenuDto mlMenuDto) {
		int isOverLap = mlMenuDao.countOverlappingMenus(mlMenuDto);
		if(isOverLap > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getMlmenuId(MlMenuDto mlMenuDto) {
		return mlMenuDao.getMlmenuId(mlMenuDto);
	}

	@Override
	public MlMenuDto selectOne(MlMenuDto mlMenuDto) {
		return mlMenuDao.selectOne(mlMenuDto);
	}
	
	

}
