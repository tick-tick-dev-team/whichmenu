package com.ticktick.whichmenu_backend.web.atch.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.atch.dao.AtchFileDAO;
import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;

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
	
	@Override
	public void insertFileMeta(AtchFileDto dto) {
	    atchFileDao.insertFileMeta(dto);
	}
	
	@Override
	public List<AtchFileDto> getFilesByReferId(int atchReferId) {
	    return atchFileDao.selectFilesByReferId(atchReferId);
	}
	
	@Override
	public void updateFileMeta(AtchFileDto dto) {
		atchFileDao.updateFileMeta(dto);
	}

	@Override
	public List<AtchFileDto> findFilesByReferId(AtchFileDto dto) {
		return atchFileDao.selectFilesByReferIdAndType(dto);
	}
	

}
