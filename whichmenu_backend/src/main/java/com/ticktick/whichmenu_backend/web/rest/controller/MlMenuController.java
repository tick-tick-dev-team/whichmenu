package com.ticktick.whichmenu_backend.web.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;
import com.ticktick.whichmenu_backend.web.atch.service.AtchFileService;
import com.ticktick.whichmenu_backend.web.rest.dao.dto.MlMenuDto;
import com.ticktick.whichmenu_backend.web.rest.service.MlMenuService;

/**
 * 메인 식단 Controller
 * 
 * */

@RequestMapping("/api/mlmeu")
@RestController
public class MlMenuController {

	private MlMenuService mlMenuService;
	
	private AtchFileService atchFileService;
	
	private static final Logger log = LoggerFactory.getLogger(MlMenuController.class);
	
	public MlMenuController(@Qualifier("mlMenuServiceImpl") MlMenuService mlMenuService,
							@Qualifier("atchFileServiceImpl") AtchFileService atchFileService) {
		this.mlMenuService   = mlMenuService;
		this.atchFileService = atchFileService;
	}
	
	@PostMapping("/rest")
	public Map<String, Object> selectOneRestMenu(@RequestBody MlMenuDto inputDto) {
		Map<String, Object> result = new HashMap<>();
		
		log.error("[식단 정보 조회] => {} ", inputDto);
		
		if ( inputDto == null || inputDto.getRestId() == null || inputDto.getRestId().isEmpty()) {
			result.put("result" , "fail");
			result.put("message", "식당ID가 존재하지 않습니다. 관리자에게 문의하시기 바랍니다.");
			return result;
		}
		else {
			try {
				MlMenuDto rsltDto = new MlMenuDto();
				rsltDto = mlMenuService.selectOneRestMenu(inputDto);
				
				result.put("MlMenuDto", rsltDto);
				
				//첨부파일 조회
				if(!rsltDto.getMlMenuId().isEmpty()) {
					AtchFileDto atchInputDto = new AtchFileDto();
					atchInputDto.setAtchReferId(rsltDto.getMlMenuId());
					atchInputDto.setRefType("M");
					List<AtchFileDto> atchRsltDto = atchFileService.findFilesByReferId(atchInputDto);
					
					//어차피 한건만 조회가 될껀데...여기서 거르는 작업을 할지 고민...
					
					result.put("atchList", atchRsltDto);
				}
				result.put("result", "success");
				
			} catch ( Exception e) {
				result.put("result" , "fail");
				result.put("message", "식단 조회 중 오류 발생: " + e.getMessage());
			}
		}
		return result;
	}
	
}
