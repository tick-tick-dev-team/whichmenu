package com.ticktick.whichmenu_backend.web.mmgr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.mmgr.dao.MngrInfoDAO;
import com.ticktick.whichmenu_backend.web.mmgr.dao.dto.MngrInfoDto;

/**
 * 관리자 정보 ServiceImpl
 * 
 * */

@Service
public class MngrInfoServiceImpl implements MngrInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(MngrInfoServiceImpl.class);
	
	private MngrInfoDAO mngrInfoDao;
	
	public MngrInfoServiceImpl(MngrInfoDAO mngrInfoDao) {
		this.mngrInfoDao = mngrInfoDao;
	}

	@Override
	public void selectAllMngrs() {
		
		List<MngrInfoDto> list = mngrInfoDao.selectAllMngrs();
		
		log.error(" 관리지 목록 리스트 ==================> {}", list.toString());
		//System.err.println(list.get(1).toString());
	}
	
    

}
