package com.ticktick.whichmenu_backend.web.mmgr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.mmgr.dao.MngrInfoDAO;
import com.ticktick.whichmenu_backend.web.mmgr.dao.MngrLogDAO;
import com.ticktick.whichmenu_backend.web.mmgr.dao.dto.MngrInfoDto;
import com.ticktick.whichmenu_backend.web.mmgr.dao.dto.MngrLogDto;

/**
 * 관리자 정보 ServiceImpl
 * 
 * */

@Service
public class MngrLogServiceImpl implements MngrLogService {
	
	private static final Logger log = LoggerFactory.getLogger(MngrLogServiceImpl.class);
	
	private MngrLogDAO mngrLogDao;
	
	public MngrLogServiceImpl(MngrLogDAO mngrLogDao) {
		this.mngrLogDao = mngrLogDao;
	}

	@Override
	public List<MngrLogDto> selectMngrLogList(MngrLogDto mngrLogDto) {
		
		List<MngrLogDto> list = mngrLogDao.selectMngrLogList(mngrLogDto);
		
		return list;
	}
	
}
