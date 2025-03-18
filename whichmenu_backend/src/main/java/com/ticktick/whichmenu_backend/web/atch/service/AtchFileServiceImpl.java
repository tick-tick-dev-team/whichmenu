package com.ticktick.whichmenu_backend.web.atch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.atch.dao.AtchFileDAO;

/**
 * 첨부파일 ServiceImpl
 * 
 * */

@Service
public class AtchFileServiceImpl implements AtchFileService {
	
	private static final Logger log = LoggerFactory.getLogger(AtchFileServiceImpl.class);
	
	private AtchFileDAO atchFileDao;
	
	public AtchFileServiceImpl(AtchFileDAO atchFileDao) {
		this.atchFileDao = atchFileDao;
	}

}
