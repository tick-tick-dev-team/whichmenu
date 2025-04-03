package com.ticktick.whichmenu_backend.web.bbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ticktick.whichmenu_backend.web.bbs.dao.dto.BbsDto;
import com.ticktick.whichmenu_backend.web.bbs.service.BbsService;

/**
 * 게시판 Controller
 * 
 * */
@RequestMapping("/bbs")
@Controller
public class BbsController {
	
	private final BbsService bbsService;
	
	public BbsController(@Qualifier("bbsServiceImpl") BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	/* 수정되는 테스트용 */
	
	@GetMapping("/list")
	public String bbsList() {
		BbsDto inputDto = new BbsDto();
		List<BbsDto> bbsListDto = bbsService.selectAllBbs(inputDto);
		
		System.err.println(bbsListDto.toString());
		return "index";
	}
	
	@GetMapping("/detail")
	public String bbsDetail() {
		BbsDto inputDto = new BbsDto();
		BbsDto rlstDto  = new BbsDto();
		rlstDto = bbsService.selectOne(inputDto);
		return "index";
	}
	
	@GetMapping("/insert")
	public String bbsInsert() {
		BbsDto inputDto = new BbsDto();
		bbsService.insertBbs(inputDto);
		return "index";
	}
	
	@GetMapping("/update")
	public String bbsUpdate() {
		BbsDto inputDto = new BbsDto();
		bbsService.updateBbs(inputDto);
		return "index";
	}
	
	@GetMapping("/delete")
	public String bbsDelete() {
		BbsDto inputDto = new BbsDto();
		bbsService.deleteBbs(inputDto);
		return "index";
	}

}
