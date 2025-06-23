package com.ticktick.whichmenu_backend.web.mmgr.service;

import java.util.List;

import com.ticktick.whichmenu_backend.web.mmgr.dao.dto.MngrLogDto;

/**
 * 관리자 정보 Service
 * 
 * */

public interface MngrLogService {

	public List<MngrLogDto> selectMngrLogList(MngrLogDto mngrLogDto);
}
