package com.ticktick.whichmenu_backend.web.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.rest.dao.RestInfoDAO;

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
	
	
}
