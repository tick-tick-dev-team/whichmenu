package com.ticktick.whichmenu_backend.web.atch.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.ticktick.whichmenu_backend.web.atch.service.AtchFileService;

/**
 * 첨부파일 Controller
 * 
 * */

@Controller
public class AtchFileController {
	
	private final AtchFileService atchFileService;
	
	public AtchFileController(@Qualifier("atchFileServiceImpl") AtchFileService atchFileService) {
		this.atchFileService = atchFileService;
	}

}
