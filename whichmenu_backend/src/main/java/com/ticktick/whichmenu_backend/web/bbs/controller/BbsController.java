package com.ticktick.whichmenu_backend.web.bbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticktick.whichmenu_backend.web.bbs.dao.dto.BbsDto;
import com.ticktick.whichmenu_backend.web.bbs.service.BbsService;

/**
 * 게시판 Controller
 * 
 * */
@RequestMapping("/bbs")
@RestController
public class BbsController {
	
	private final BbsService bbsService;
	
	public BbsController(@Qualifier("bbsServiceImpl") BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	/* 수정되는 테스트용 */
	
	@GetMapping("/list")
	public List<BbsDto> bbsList(BbsDto inputDto) {
		List<BbsDto> bbsListDto = bbsService.selectAllBbs(inputDto);
		
		System.err.println(bbsListDto.toString());
		return bbsListDto;
	}
	
	@GetMapping("/detail")
	public BbsDto bbsDetail(BbsDto inputDto) {
		
		if (inputDto.getBbsId() == null || inputDto.getBbsId().isBlank()) {
	        throw new IllegalArgumentException("게시글 ID(bbsId)는 필수입니다.");
	    }
		
		BbsDto rlstDto  = new BbsDto();
		rlstDto = bbsService.selectOne(inputDto);
		return rlstDto;
	}
	
	@GetMapping("/insert")
	public String bbsInsert(BbsDto inputDto) {
		bbsService.insertBbs(inputDto);
		return "index";
	}
	
	@GetMapping("/update")
	public String bbsUpdate(BbsDto inputDto) {
		bbsService.updateBbs(inputDto);
		return "index";
	}
	
	@GetMapping("/delete")
	public String bbsDelete(BbsDto inputDto) {
		bbsService.deleteBbs(inputDto);
		return "index";
	}

}
