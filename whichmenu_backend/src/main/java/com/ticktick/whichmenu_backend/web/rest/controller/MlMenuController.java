package com.ticktick.whichmenu_backend.web.rest.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;
import com.ticktick.whichmenu_backend.web.atch.service.AtchFileService;
import com.ticktick.whichmenu_backend.web.rest.dao.dto.MlMenuDto;
import com.ticktick.whichmenu_backend.web.rest.service.MlMenuService;

/**
 * 메인 식단 Controller
 * 
 * */

@RequestMapping("/api/mlmenu")
@RestController
public class MlMenuController {

	private MlMenuService mlMenuService;
	
	private AtchFileService atchFileService;
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
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
	
	
	@GetMapping("/overlap-check")
	public Map<String, Object> mlmenuOverlapCheck(
		@RequestParam("bgngDt") String bgngDt,
		@RequestParam("endDt")  String endDt,
		@RequestParam(value = "restId"  , required = false) String restId,
		@RequestParam(value = "mlMenuId", required = false) String mlMenuId) {
		
		Map<String, Object> result = new HashMap<>();
		boolean isOverLap = false;
		
		if (bgngDt != null && !bgngDt.isEmpty() &&
			endDt  != null && !endDt.isEmpty()  &&
			restId != null && !restId.isEmpty()) {
			
			MlMenuDto inputDto = new MlMenuDto();
			inputDto.setMlMenuId(mlMenuId);
			inputDto.setRestId(restId);
			inputDto.setBgngDt(bgngDt);
			inputDto.setEndDt(endDt);
			isOverLap = mlMenuService.mlmenuOverlapCheck(inputDto);
			log.error("[★★★★★★★★ 1) 식단 개시기간 날짜 중복 체크 ★★★★★★★★] => {} ", isOverLap);
		}
		
		result.put("isOverLap", isOverLap);
		return result;
	}
	
	@PostMapping("/register")
	public Map<String, Object> registerMenu(@ModelAttribute MlMenuDto inputDto,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "url", required = false) String url){
		
		Map<String, Object> result = new HashMap<>();
		String message = "";
		String rslt = "fail";
		
		log.error("[식단 등록 정보 Dto] => {} ", inputDto);
		/*
		 * 1. 입력값 체크
		 * */
		if (	inputDto == null
			||	inputDto.getRestId() 		== null 	|| inputDto.getRestId().isEmpty()
			||	inputDto.getBgngDt() 		== null 	|| inputDto.getBgngDt().isEmpty()
			||	inputDto.getEndDt()			== null		|| inputDto.getEndDt().isEmpty()
			||	inputDto.getInfoInitType() 	== null 	|| inputDto.getInfoInitType().isEmpty()
			||	inputDto.getUseYn() 		== null 	|| inputDto.getUseYn().isEmpty()
		) {
			message = "입력값이 부족합니다.";
		} else {
			
			if ("URL".equals(inputDto.getInfoInitType())) {
				inputDto.setOutsdReferUrl(url);
			}
			mlMenuService.insertMlMenu(inputDto);
			rslt = "success";
			message = "등록 성공";
			
			if ("FILE".equals(inputDto.getInfoInitType()) && file != null && !file.isEmpty()) {
				// 파일 저장 처리
				// 예: fileService.saveFile(file);
				String mlMenuId = mlMenuService.getMlmenuId(inputDto);
				
				//  파일명까지 포함된 “전체 파일 경로” 생성
				String originalFileName = file.getOriginalFilename();
				String fileExtn = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				
				// 중복이 없도록 생성
				String savePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + originalFileName;
				
				File dest = new File(savePath);
				dest.getParentFile().mkdirs();
				
				try {
					file.transferTo(dest);
				} catch (IOException e) {
					log.error("파일 저장 중 오류", e);
					message = "파일 저장 실패";
					rslt = "fail";
					
					result.put("result", rslt);
					result.put("message", message);
					return result;
				}
				
				AtchFileDto fileDto = new AtchFileDto();
				
				fileDto.setAtchReferId(mlMenuId);
				fileDto.setFileOdr(1);
				fileDto.setFileNm(originalFileName);
				fileDto.setFilePath(savePath);
				fileDto.setFileSz((int) file.getSize());
				fileDto.setFileExtn(fileExtn);
				fileDto.setRefType("M");
				
				atchFileService.insertFileMeta(fileDto);
				
			}
		}
		result.put("result" , rslt);
		result.put("message", message);
		return result;
	}
	
	@GetMapping("/{id}")
	public Map<String, Object> getMenuById(@PathVariable("id") String id) {
		
		Map<String, Object> result = new HashMap<>();
		String message = "";
		
		if (id == null || "".equals(id)) {
			message = "입력값이 부족합니다.";
			result.put("message", message);
		} else {
			MlMenuDto inputDto = new MlMenuDto();
			inputDto.setMlMenuId(id);
			inputDto = mlMenuService.selectOne(inputDto);
			if(inputDto != null && "WEEK".equals(inputDto.getInfoInitType())){
				AtchFileDto atchInputDto = new AtchFileDto();
				
				atchInputDto.setAtchReferId(inputDto.getMlMenuId());
				atchInputDto.setRefType("M");
				
				List<AtchFileDto> atchRsltDto = atchFileService.findFilesByReferId(atchInputDto);
				if(atchRsltDto.size() > 0) {
					inputDto.setFileList(atchRsltDto.get(0));
				}
			}
			result.put("data", inputDto);
		}

		return result;
	}
	
	@PutMapping("/{id}")
	public Map<String, Object> updateMenu(
		@PathVariable("id") String id,
		@ModelAttribute MlMenuDto inputDto,
		@RequestParam(value = "file"		, required = false) MultipartFile file,
		@RequestParam(value = "url" 		, required = false) String url,
		@RequestParam(value = "fileChanged"	, required = false, defaultValue = "false") boolean fileChanged
	) {
		Map<String, Object> result = new HashMap<>();
		String message = "";
		String rslt = "fail";
		
		log.error("[식단 수정 정보 Dto] => {} "	, inputDto);
		log.error("[수정 대상 ID] => {}"		, id);
		log.error("[파일 변경 여부] => {}"		, fileChanged);
		
		/*
		 * 1. 입력값 체크
		 * */
		if (id == null 		|| id.isEmpty()			|| inputDto == null
			|| inputDto.getRestId() == null			|| inputDto.getRestId().isEmpty()
			|| inputDto.getBgngDt() == null			|| inputDto.getBgngDt().isEmpty()
			|| inputDto.getEndDt()  == null			|| inputDto.getEndDt().isEmpty()
			|| inputDto.getInfoInitType() == null	|| inputDto.getInfoInitType().isEmpty()
			|| inputDto.getUseYn() == null			|| inputDto.getUseYn().isEmpty()
		) {
			message = "입력값이 부족합니다.";
			result.put("result", rslt);
			result.put("message", message);
			return result;
		}
		
		try {
			inputDto.setMlMenuId(id);
			
			/*
			 * 2. 정보유형개시 체크 후 기본 식단 정보 수정
			 * */
			if ("URL".equals(inputDto.getInfoInitType())) {
				inputDto.setOutsdReferUrl(url);
			}
			mlMenuService.updateMlMenu(inputDto);
		
			/*
			 * 3. 파일 정보 처리
			 *  정보개시유형이 : FILE 이고, fileChanged (파일이 수정되었는지 Y/N 여부)
			 * */
			if ("FILE".equals(inputDto.getInfoInitType())) {
				/*
				 * 4. fileChanged true 이면 
				 * 새파일이 있던 없던 기존 파일은 삭제 처리 후 새로 등록 또는 업데이트
				 * */
				if (fileChanged) {
					
					/*
					 * 5. 기존 파일 논리삭제 처리
					 * */
					AtchFileDto srchFileDto = new AtchFileDto();
					srchFileDto.setAtchReferId(id);
					srchFileDto.setRefType("M");
					List<AtchFileDto> atchRsltDto = atchFileService.findFilesByReferId(srchFileDto);
					if(atchRsltDto.size() > 0) {
						srchFileDto.setAtchFileId(atchRsltDto.get(0).getAtchFileId());
						atchFileService.updateFileMeta(srchFileDto);
					}
					
					/*
					 * 6. 새 파일 저장
					 * */
					if (file != null && !file.isEmpty()) {
						String originalFileName = file.getOriginalFilename();
						String fileExtn = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
						String savePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + originalFileName;
						
						File dest = new File(savePath);
						dest.getParentFile().mkdirs();
						
						try {
							file.transferTo(dest);
						} catch (IOException e) {
							log.error("파일 저장 중 오류", e);
							message = "파일 저장 실패";
							rslt = "fail";
							result.put("result", rslt);
							result.put("message", message);
							return result;
						}
						
						AtchFileDto fileDto = new AtchFileDto();
						fileDto.setAtchReferId(id);
						fileDto.setFileOdr(1);
						fileDto.setFileNm(originalFileName);
						fileDto.setFilePath(savePath);
						fileDto.setFileSz((int) file.getSize());
						fileDto.setFileExtn(fileExtn);
						fileDto.setRefType("M");
						
						atchFileService.insertFileMeta(fileDto);
						
					}
				}
			}
			
			rslt = "success";
			message = "수정 성공";
			
		} catch (Exception e) {
			log.error("식단 수정 중 오류 발생", e);
			message = "수정 실패: " + e.getMessage();
			rslt = "fail";
		}
		
		result.put("result", rslt);
		result.put("message", message);
		return result;
	}


}
