package com.ticktick.whichmenu_backend.web.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	private static final Logger log = LoggerFactory.getLogger(RestInfoContorller.class);
	
	public RestInfoContorller(@Qualifier("restInfoServiceImpl") RestInfoService restInfoService) {
		this.restInfoService = restInfoService;
	}

	@GetMapping("/list")
	public List<RestInfoDto> restList(@RequestParam("useYn") String useYn, @RequestParam(value = "srchKwrd", required = false) String srchKwrd) {
		
		RestInfoDto inputDto = new RestInfoDto();
		inputDto.setUseYn(useYn);
		inputDto.setSrchKwrd(srchKwrd);
		List<RestInfoDto> restListDto = restInfoService.selectRestList(inputDto);
		
		log.error("[식당리스트] => {} ", restListDto);
		return restListDto;
	}
	
	@PostMapping("/insert")
	public Map<String, Object> restInsert(@RequestBody RestInfoDto inputDto){
		
		Map<String, Object> result = new HashMap<>();
		log.error("[식당정보등록] => {} ", inputDto);
		
		if (   inputDto == null
			|| inputDto.getRestNm()       == null || inputDto.getRestNm().isEmpty()
			|| inputDto.getRestAddr()     == null || inputDto.getRestAddr().isEmpty()
			|| inputDto.getInfoInitType() == null || inputDto.getInfoInitType().isEmpty()
			|| inputDto.getUseYn()        == null || inputDto.getUseYn().isEmpty()) {
			result.put("result" , "fail");
			result.put("message", "입력값이 부족합니다.");
			return result;
		}
		else {
			try {
				restInfoService.insertRestInfo(inputDto);
				result.put("result", "success");
			} catch (Exception e) {
				result.put("result" , "fail");
				result.put("message", "식당 등록 중 오류 발생: " + e.getMessage());
			}
		}
		return result;
	}
	
	@PostMapping("/delete")
	public Map<String, Object> restDelete(@RequestBody RestInfoDto inputDto){
		Map<String, Object> result = new HashMap<>();
		log.error("[식당정보삭제] => {} ", inputDto);
		
		if ( inputDto == null || inputDto.getRestId() == null || inputDto.getRestId().isEmpty()) {
			result.put("result" , "fail");
			result.put("message", "식당ID가 존재하지 않습니다.");
			return result;
		}
		else {
			try {
				restInfoService.deleteRestInfo(inputDto);
				result.put("result", "success");
			} catch (Exception e) {
				result.put("result" , "fail");
				result.put("message", "식당 삭제 중 오류 발생: " + e.getMessage());
			}
		}
		return result;
	}
	
	@PostMapping("/update")
	public Map<String, Object> restUpdate(@RequestBody RestInfoDto inputDto){
		
		Map<String, Object> result = new HashMap<>();
		log.error("[식당정보수정] => {} ", inputDto);
		
		if (   inputDto == null
			|| inputDto.getRestNm()       == null || inputDto.getRestNm().isEmpty()
			|| inputDto.getRestAddr()     == null || inputDto.getRestAddr().isEmpty()
			|| inputDto.getInfoInitType() == null || inputDto.getInfoInitType().isEmpty()
			|| inputDto.getUseYn()        == null || inputDto.getUseYn().isEmpty()) {
			result.put("result" , "fail");
			result.put("message", "입력값이 부족합니다.");
			return result;
		}
		else {
			try {
				restInfoService.updateRestInfo(inputDto);
				result.put("result", "success");
			} catch (Exception e) {
				result.put("result" , "fail");
				result.put("message", "식당 수정 중 오류 발생: " + e.getMessage());
			}
		}
		return result;
	}
}
