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
	public void insertMlMenu(MlMenuDto mlMenuDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMlMenu(MlMenuDto mlMenuDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMlMenu(MlMenuDto mlMenuDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<MlMenuDto> selectListRestMlMenu(MlMenuDto mlMenuDto) {
		List<MlMenuDto> rsltList = mlMenuDao.selectListRestMlMenu(mlMenuDto);
		
		
		return rsltList;
	}
	
	

}
