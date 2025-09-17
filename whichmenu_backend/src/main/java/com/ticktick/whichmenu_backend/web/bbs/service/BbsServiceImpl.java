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
	public List<BbsDto> selectAllBbs(BbsDto inputDto) {
		List<BbsDto> bbsListDto = bbsDao.selectAllBbs(inputDto);	
		return bbsListDto;
	}

	@Override
	public BbsDto selectOne(BbsDto bbsDto) {
		
		BbsDto rsltDto = new BbsDto();
		rsltDto = bbsDao.selectOneBbs(bbsDto);
		return rsltDto;
	}
	
	@Override
	public BbsDto selectBbsWithFiles(BbsDto bbsDto){
		return bbsDao.selectBbsWithFiles(bbsDto);
	}

	@Override
	public void insertBbs(BbsDto bbsDto) {
		bbsDao.insertBbs(bbsDto);
	}

	@Override
	public void updateBbs(BbsDto bbsDto) {
		bbsDao.updateBbs(bbsDto);
	}

	@Override
	public void deleteBbs(BbsDto bbsDto) {
		bbsDao.deleteBbs(bbsDto);
	}
	
	@Override
	public int countExistingFilesByReferId(String atchReferId) {
        return bbsDao.countExistingFilesByReferId(atchReferId);
    }

}
