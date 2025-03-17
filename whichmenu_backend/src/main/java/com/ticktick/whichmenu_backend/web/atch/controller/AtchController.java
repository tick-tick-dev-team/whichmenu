package com.ticktick.whichmenu_backend.web.atch.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.ticktick.whichmenu_backend.web.atch.service.AtchService;

/**
 * 첨부파일 Controller
 * 
 * */

@Controller
public class AtchController {
	
	private final AtchService atchService;
	
	public AtchController(@Qualifier("atchServiceImpl") AtchService atchService) {
		this.atchService = atchService;
	}

}
