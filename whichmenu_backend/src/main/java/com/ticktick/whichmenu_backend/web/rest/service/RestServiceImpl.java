package com.ticktick.whichmenu_backend.web.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.rest.dao.RestDAO;

/**
 * 식당 정보 ServiceImpl
 * 
 * */

@Service
public class RestServiceImpl implements RestService {
	
	private static final Logger log = LoggerFactory.getLogger(RestServiceImpl.class);

	private RestDAO restDao;
	
	public RestServiceImpl(RestDAO restDao) {
		this.restDao = restDao;
	}
	
	
}
