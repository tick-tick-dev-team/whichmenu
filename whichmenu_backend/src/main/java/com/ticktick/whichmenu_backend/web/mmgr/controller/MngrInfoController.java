package com.ticktick.whichmenu_backend.web.mmgr.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.ticktick.whichmenu_backend.web.mmgr.service.MngrInfoService;

/**
 * 관리자 정보 Controller
 * 
 * */

@Controller
public class MngrInfoController {
	
	private final MngrInfoService mngrInfoService;
	
	public MngrInfoController(@Qualifier("mngrInfoServiceImpl") MngrInfoService mngrInfoService) {
		this.mngrInfoService = mngrInfoService;
	}

}
