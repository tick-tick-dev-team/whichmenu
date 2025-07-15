package com.ticktick.whichmenu_backend.web.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		
		String message = "";
		String rslt = "fail";
		
		log.error("[식단 정보 조회] => {} ", inputDto);
		
		/*
		 * 1. 입력값 체크
		 * */
		if (inputDto == null ) {
			message = "검색조건이 비어있습니다. 관리자에게 문의하시기 바랍니다.";
		} else if (inputDto.getRestId() == null || inputDto.getRestId().isEmpty()) {
			message = "식당 ID 값이 존재하지 않습니다. 관리자에게 문의하시기 바랍니다.";
		} else if (inputDto.getInfoInitType() == null || inputDto.getInfoInitType().isEmpty()) {
			message = "식당 정보 유형값이 존재하지 않습니다. 관리자에게 문의하시기 바랍니다.";
		} else {
			
			/*
			 * 2. 서비스 호출
			 * */
			try {
				
				/*
				 * 3-1. 정보개시유형 (일간 DAY ) - 단건 조회
				 * 
				 * */
				if("DAY".equals(inputDto.getInfoInitType())) {
					MlMenuDto rsltDto = new MlMenuDto();
					
					log.error("[★★★★★★★★ 3-1. 식단 단건 조회 호출 ★★★★★★★★] => {} ", inputDto);
					rsltDto = mlMenuService.selectOneRestMenu(inputDto);
					log.error("[★★★★★★★★ 3-1. 식단 단건 조회 결과값 ★★★★★★★] => {} ", rsltDto);
					
					result.put("MlMenuDto", rsltDto);
				} else {
					
					/*
					 * 3-2. 정보개시유형 (주간 WEEK ) - 다건 조회
					 * 
					 * 1) 검색조건 날짜의 3건의 식단 조회
					 *    저번주, 현재, 다음주 식단 총 3건 조회
					 * */
					List<MlMenuDto> rsltListDto = new ArrayList<MlMenuDto>();
					log.error("[★★★★★★★★ 3-2. 식단 다건 조회 호출 ★★★★★★★★] => {} ", inputDto);
					rsltListDto = mlMenuService.selectListRestMlMenu(inputDto);
					log.error("[★★★★★★★★ 3-2. 식단 다건 조회 결과값 ★★★★★★★] => {} ", rsltListDto);
					
					if(rsltListDto.size() > 0) {
						
						/*
						 * 2) 현재 식단 첨부파일 조회 (POST_TYPE 비교)
						 *    현재의 식단이 존재하는지 체크 후 해당 식단의 첨부파일을 조회
						 * */
						Optional<MlMenuDto> currentMenuOpt = rsltListDto.stream()
								.filter(currentMenu -> "CURRENT".equals(currentMenu.getPosType()))
								.findFirst();
						
						log.error("[★★★★★★★★ 2) Optional 현재식단 값 여부 조회 ★★★★★★★] => {} ", currentMenuOpt);
						if(currentMenuOpt.isPresent()) {
							
							
							MlMenuDto currentMenu = currentMenuOpt.get();
							log.error("[★★★★★★★★ 3) Optional 현재식단 ★★★★★★★] => {} ", currentMenu);
							AtchFileDto atchInputDto = new AtchFileDto();
							
							atchInputDto.setAtchReferId(currentMenu.getMlMenuId());
							atchInputDto.setRefType("M");
							
							log.error("[★★★★★★★★ 4) 첨부파일 조회 호출 ★★★★★★★★] => {} " , atchInputDto);
							List<AtchFileDto> atchRsltDto = atchFileService.findFilesByReferId(atchInputDto);
							log.error("[★★★★★★★★ 5) 첨부파일 조회 결과값 ★★★★★★★★] => {} ", atchRsltDto);
							
							result.put("atchList", atchRsltDto);
						}
						result.put("MlMenuDto", rsltListDto);
					}
				}
				rslt = "success";
			} catch (Exception e) {
				rslt = "fail";
				message =  "식단 조회 중 오류 발생: " + e.getMessage();
			}
		}
		result.put("result"  , rslt);
		result.put("message" , message);
		
		return result;
	}
	
}
