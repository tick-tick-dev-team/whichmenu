package com.ticktick.whichmenu_backend.web.rest.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.ticktick.whichmenu_backend.web.rest.service.RestInfoService;

/**
 * 식당 Controller
 * 
 * */

@Controller
public class RestInfoContorller {
	
	private final RestInfoService restInfoService;
	
	public RestInfoContorller(@Qualifier("restInfoServiceImpl") RestInfoService restInfoService) {
		this.restInfoService = restInfoService;
	}

}
