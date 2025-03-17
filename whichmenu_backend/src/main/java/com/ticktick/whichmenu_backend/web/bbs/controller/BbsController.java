package com.ticktick.whichmenu_backend.web.bbs.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.ticktick.whichmenu_backend.web.bbs.service.BbsService;

/**
 * 게시판 Controller
 * 
 * */

@Controller
public class BbsController {
	
	private final BbsService bbsService;
	
	public BbsController(@Qualifier("bbsServiceImpl") BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	/* 수정되는 테스트용 */

}
