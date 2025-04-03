package com.ticktick.whichmenu_backend.web.bbs.service;

import java.util.List;

import com.ticktick.whichmenu_backend.web.bbs.dao.dto.BbsDto;

public interface BbsService {
	
	public List<BbsDto> selectAllBbs(BbsDto bbsDto);
	
	public BbsDto selectOne(BbsDto bbsDto);
	
	public void insertBbs(BbsDto bbsDto);
	
	public void updateBbs(BbsDto bbsDto);
	
	public void deleteBbs(BbsDto bbsDto);

}
