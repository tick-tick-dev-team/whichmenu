package com.ticktick.whichmenu_backend.web.rest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.rest.dao.RestInfoDAO;
import com.ticktick.whichmenu_backend.web.rest.dao.dto.RestInfoDto;

/**
 * 식당 정보 ServiceImpl
 * 
 * */

@Service
public class RestInfoServiceImpl implements RestInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(RestInfoServiceImpl.class);

	private RestInfoDAO restInfoDao;
	
	public RestInfoServiceImpl(RestInfoDAO restInfoDao) {
		this.restInfoDao = restInfoDao;
	}

	@Override
	public List<RestInfoDto> selectRestList(RestInfoDto inputDto) {
		List<RestInfoDto> restListDto = restInfoDao.selectRestList(inputDto);
		return restListDto;
	}

	@Override
	public RestInfoDto selectOneRest(RestInfoDto inputDto) {
		RestInfoDto rsltDto = restInfoDao.selectOneRest(inputDto);
		return rsltDto;
	}

	@Override
	public void insertRestInfo(RestInfoDto inputDto) {
		restInfoDao.insertRestInfo(inputDto);
	}

	@Override
	public void updateRestInfo(RestInfoDto inputDto) {
		restInfoDao.updateRestInfo(inputDto);
	}

	@Override
	public void deleteRestInfo(RestInfoDto inputDto) {
		restInfoDao.deleteRestInfo(inputDto);
	}
	
	
}
