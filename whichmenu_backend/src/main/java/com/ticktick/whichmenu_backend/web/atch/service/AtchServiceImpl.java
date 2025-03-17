package com.ticktick.whichmenu_backend.web.atch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.atch.dao.AtchDAO;

/**
 * 첨부파일 ServiceImpl
 * 
 * */

@Service
public class AtchServiceImpl implements AtchService {
	
	private static final Logger log = LoggerFactory.getLogger(AtchServiceImpl.class);
	
	private AtchDAO atchDao;
	
	public AtchServiceImpl(AtchDAO atchDao) {
		this.atchDao = atchDao;
	}

}
