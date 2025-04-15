package com.ticktick.whichmenu_backend.web.bbs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticktick.whichmenu_backend.web.bbs.dao.dto.BbsDto;
import com.ticktick.whichmenu_backend.web.bbs.service.BbsService;

/**
 * 게시판 Controller
 * 
 * */
@RequestMapping("/api/bbs")
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
	
	@PostMapping("/insert")
	public Map<String, Object> bbsInsert(@RequestBody BbsDto inputDto) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 1. inputDto null 체크 및 필수값 체크
	    if (inputDto == null
	    	    || inputDto.getBbsTtl() == null || inputDto.getBbsTtl().isEmpty()
	    	    || inputDto.getBbsCn() == null || inputDto.getBbsCn().isEmpty()
	    	    || inputDto.getRegNm() == null || inputDto.getRegNm().isEmpty()
	    	    || inputDto.getBbsType() == null || inputDto.getBbsType().isEmpty()
	    	    || inputDto.getUseYn() == null || inputDto.getUseYn().isEmpty()
	    	    || inputDto.getRlsYn() == null || inputDto.getRlsYn().isEmpty()) {
	    	result.put("result", "fail");
            result.put("message", "입력값이 부족합니다.");
	        return result;
	    }
		
		try {
	        // 2. 서비스 로직 실행
	        bbsService.insertBbs(inputDto);
	        result.put("result", "success");
	    } catch (Exception e) {
	        // 예외 발생 시 처리
	    	result.put("result", "fail");
            result.put("message", "등록 중 오류 발생: " + e.getMessage());
	    }
		return result;
	}
	
	@PostMapping("/update")
	public Map<String, Object> bbsUpdate(@RequestBody BbsDto inputDto) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 1. inputDto null 체크 및 필수값 체크
		if (inputDto == null
				|| inputDto.getBbsId() == null || inputDto.getBbsId().isBlank()
	    	    || inputDto.getBbsTtl() == null || inputDto.getBbsTtl().isEmpty()
	    	    || inputDto.getBbsCn() == null || inputDto.getBbsCn().isEmpty()
	    	    || inputDto.getRegNm() == null || inputDto.getRegNm().isEmpty()
	    	    || inputDto.getBbsType() == null || inputDto.getBbsType().isEmpty()
	    	    || inputDto.getUseYn() == null || inputDto.getUseYn().isEmpty()
	    	    || inputDto.getRlsYn() == null || inputDto.getRlsYn().isEmpty()) {
	    	result.put("result", "fail");
            result.put("message", "입력값이 부족합니다.");
	        return result;
	    }
		
		// 2. 서비스 로직 실행
	    try {
            bbsService.updateBbs(inputDto);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("result", "fail");
            result.put("message", "수정 중 오류 발생: " + e.getMessage());
        }
	    
		return result;
	}
	
	@PostMapping("/delete")
	public  Map<String, Object> bbsDelete(@RequestBody BbsDto inputDto) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 1. inputDto null 체크 및 필수값 체크
		if (inputDto == null
			|| inputDto.getBbsId() == null || inputDto.getBbsId().isBlank()
			) {
	    	result.put("result", "fail");
            result.put("message", "입력값이 부족합니다.");
	        return result;
	    }
		
		// 2. 서비스 로직 실행
		try {
            bbsService.deleteBbs(inputDto);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("result", "fail");
            result.put("message", "삭제 중 오류 발생: " + e.getMessage());
        }
		return result;
	}

}
