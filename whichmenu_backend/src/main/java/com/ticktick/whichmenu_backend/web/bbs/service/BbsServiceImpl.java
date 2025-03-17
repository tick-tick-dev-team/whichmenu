package com.ticktick.whichmenu_backend.web.bbs.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticktick.whichmenu_backend.web.bbs.dao.BbsDAO;
import com.ticktick.whichmenu_backend.web.bbs.dao.dto.BbsDto;

/**
 * 게시판 ServiceImpl
 * 
 * */

@Service
public class BbsServiceImpl implements BbsService {
	
	private static final Logger log = LoggerFactory.getLogger(BbsServiceImpl.class);
	
	private BbsDAO bbsDao;
	
	public BbsServiceImpl(BbsDAO bbsDao) {
		this.bbsDao = bbsDao;
	}

	@Override
	public List<BbsDto> selectAllBbs() {
		return null;
	}

	@Override
	public BbsDto selectOne(BbsDto bbsDto) {
		return null;
	}

	@Override
	public void insertBbs(BbsDto bbsDto) {
		
	}

	@Override
	public void updateBbs(BbsDto bbsDto) {
		
	}

	@Override
	public void deleteBbs(BbsDto bbsDto) {
		
	}

}
