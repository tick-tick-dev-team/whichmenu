package com.ticktick.whichmenu_backend.web.rest.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.ticktick.whichmenu_backend.web.rest.service.RestService;

/**
 * 식당 Controller
 * 
 * */

@Controller
public class RestContorller {
	
	private final RestService restService;
	
	public RestContorller(@Qualifier("restServiceImpl") RestService restService) {
		this.restService = restService;
	}

}
