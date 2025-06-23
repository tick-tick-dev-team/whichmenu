package com.ticktick.whichmenu_backend.web.mmgr.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticktick.whichmenu_backend.web.mmgr.dao.dto.MngrLogDto;
import com.ticktick.whichmenu_backend.web.mmgr.service.MngrLogService;
import com.ticktick.whichmenu_backend.web.rest.controller.RestInfoContorller;

/**
 * 관리자 접속 기록 Controller
 * 
 * */

@RequestMapping("/api/mngrLog")
@RestController
public class MngrLogController {
	
	private final MngrLogService mngrLogService;
	
	private static final Logger log = LoggerFactory.getLogger(RestInfoContorller.class);
	
	public MngrLogController(@Qualifier("mngrLogServiceImpl") MngrLogService mngrLogService) {
		this.mngrLogService = mngrLogService;
	}
	
	@PostMapping("/list")
	public List<MngrLogDto> selectMngrLogList(@RequestBody MngrLogDto inputDto) {
		log.error("[관리자접속 검색조건] => {} ", inputDto);
		List<MngrLogDto> mngrLogListDto = mngrLogService.selectMngrLogList(inputDto);		
		
		log.error("[관리자접속리스트] => {} ", mngrLogListDto);
		return mngrLogListDto;
	}
	

}
