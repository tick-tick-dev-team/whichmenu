package com.ticktick.whichmenu_backend.web.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticktick.whichmenu_backend.web.rest.dao.dto.RestInfoDto;
import com.ticktick.whichmenu_backend.web.rest.service.RestInfoService;

/**
 * 식당 Controller
 * 
 * */

@RequestMapping("/api/rest")
@RestController
public class RestInfoContorller {
	
	private final RestInfoService restInfoService;
	
	public RestInfoContorller(@Qualifier("restInfoServiceImpl") RestInfoService restInfoService) {
		this.restInfoService = restInfoService;
	}

	@GetMapping("/list")
	public List<RestInfoDto> restList(RestInfoDto inputDto) {
		List<RestInfoDto> restListDto = restInfoService.selectRestList(inputDto);
		
		System.err.println(restListDto.toString());
		return restListDto;
	}
}
