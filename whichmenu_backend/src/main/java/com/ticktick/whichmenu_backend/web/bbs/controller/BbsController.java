package com.ticktick.whichmenu_backend.web.bbs.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;
import com.ticktick.whichmenu_backend.web.atch.service.AtchFileService;
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
	private final AtchFileService atchFileService;
	
	// application.properties에서 값 주입받음
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	public BbsController(@Qualifier("bbsServiceImpl") BbsService bbsService,
		    @Qualifier("atchFileServiceImpl") AtchFileService atchFileService) {
		this.bbsService = bbsService;
		this.atchFileService = atchFileService;
	}
	
	@GetMapping("/list")
	public List<BbsDto> bbsList(BbsDto inputDto) {
		List<BbsDto> bbsListDto = bbsService.selectAllBbs(inputDto);
		
		System.err.println(bbsListDto.toString());
		return bbsListDto;
	}
	
	@GetMapping("/onlyBbsDetail")
	public BbsDto bbsDetail(BbsDto inputDto) {
		
		if (inputDto.getBbsId() == null || inputDto.getBbsId().isBlank()) {
	        throw new IllegalArgumentException("게시글 ID(bbsId)는 필수입니다.");
	    }
		
		BbsDto rlstDto  = new BbsDto();
		rlstDto = bbsService.selectOne(inputDto);
		return rlstDto;
	}
	
	@GetMapping("/detail")
	public BbsDto selectBbsWithFiles(BbsDto inputDto) {
		
		if (inputDto.getBbsId() == null || inputDto.getBbsId().isBlank()) {
	        throw new IllegalArgumentException("게시글 ID(bbsId)는 필수입니다.");
	    }
		
		BbsDto rlstDto = bbsService.selectBbsWithFiles(inputDto);
		return rlstDto;
	}
	
	@PostMapping("/insert")
	public Map<String, Object> bbsInsert(@RequestParam("files") List<MultipartFile> files,
		    @ModelAttribute AtchFileDto fileDto, @ModelAttribute BbsDto inputDto) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 1. inputDto null 체크 및 필수값 체크
	    if (inputDto == null
	    	    || inputDto.getBbsTtl() == null || inputDto.getBbsTtl().isEmpty()
	    	    || inputDto.getBbsCn() == null || inputDto.getBbsCn().isEmpty()
	    	    || inputDto.getRegNm() == null || inputDto.getRegNm().isEmpty()
	    	    || inputDto.getBbsType() == null || inputDto.getBbsType().isEmpty()
	    	    || inputDto.getRlsYn() == null || inputDto.getRlsYn().isEmpty()) { // RlsYn 게시글 공개여부
	    	result.put("result", "fail");
            result.put("message", "입력값이 부족합니다.");
	        return result;
	    }
		
		try {
			
			inputDto.setUseYn("Y");
			
	        // 서비스 로직 실행
	        bbsService.insertBbs(inputDto);
	        
	        
	        // 파일 저장
	        if(files != null && !files.isEmpty()) {
	        	BbsDto resultData = new BbsDto();
	        	try {
		        	// 저장 된 것 가지고 옴
	    	        resultData = bbsService.selectOne(inputDto);
	    	    } catch (Exception e) {
    		        // 예외 발생 시 처리
    		    	result.put("result", "fail");
    	            result.put("message", "등록한 데이터 조회 중 오류 발생: " + e.getMessage());
	    		}
	        	
	        	int order = 1; // 파일 순서
	        	for (MultipartFile file : files) {
	        		
	        		if (file.isEmpty()) continue;
	        	
		        	try {
		        		
		        		//  파일명까지 포함된 “전체 파일 경로” 생성
			    		String originalFileName = file.getOriginalFilename();
			    		String fileExtn = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
			    		// 중복이 없도록 생성
			    		String savePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + originalFileName;
			    		
			    		File dest = new File(savePath);
			    		dest.getParentFile().mkdirs(); // 디렉토리 없으면 생성
			    		file.transferTo(dest); // 실제 파일 이동
			    		
			    		// DB에 메타데이터 저장
			    		fileDto.setAtchReferId(resultData.getBbsId());
			    		fileDto.setRefType(resultData.getBbsType());
			            fileDto.setFileOdr(order++); // 기본 1로 설정
			            fileDto.setFileNm(originalFileName);
			            fileDto.setFilePath(savePath);
			            fileDto.setFileSz((int) file.getSize());
			            fileDto.setFileExtn(fileExtn);
			            
			            // DB에 저장하는 로직
			            atchFileService.insertFileMeta(fileDto);
		        	} catch (Exception e) {
		        		result.put("result", "fail");
		                result.put("message", "파일 등록 중 오류 발생: " + e.getMessage());
		        	}
	        	}
	        	
	        }
	        
	        result.put("result", "success");
	    } catch (Exception e) {
	        // 예외 발생 시 처리
	    	result.put("result", "fail");
            result.put("message", "등록 중 오류 발생: " + e.getMessage());
	    }
		return result;
	}
	
	@PostMapping("/update")
	public Map<String, Object> bbsUpdate(
	        @ModelAttribute BbsDto inputDto,
	        @RequestParam(value = "files", required = false) List<MultipartFile> files,
	        @RequestParam(value = "deletedFileIds", required = false) String deletedFileIdsJson
	        ) {

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

	    try {
	        // 2. 게시글 수정
	        bbsService.updateBbs(inputDto);
	        
	        
	     // 3. 삭제 요청된 파일 처리
	        if (deletedFileIdsJson != null && !deletedFileIdsJson.isBlank()) {
	            List<String> deletedFileIds = new ObjectMapper().readValue(
	                deletedFileIdsJson, new TypeReference<List<String>>() {}
	            );
	            for (String fileId : deletedFileIds) {
	                AtchFileDto fileDto = new AtchFileDto();
	                fileDto.setAtchFileId(fileId);
	                atchFileService.updateFileMeta(fileDto); // USE_YN = 'N' 처리
	            }
	        }

	        // 4. 새 파일 업로드 처리
	        if (files != null && !files.isEmpty()) {
                int order = 1;
                for (MultipartFile file : files) {
                    if (file.isEmpty()) continue;

                    String originalFileName = file.getOriginalFilename();
                    String fileExtn = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                    String savePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + originalFileName;

                    File dest = new File(savePath);
                    dest.getParentFile().mkdirs();
                    file.transferTo(dest);

                    AtchFileDto fileDto = new AtchFileDto();
                    fileDto.setAtchReferId(inputDto.getBbsId());
                    fileDto.setRefType(inputDto.getBbsType());
                    fileDto.setFileOdr(order++);
                    fileDto.setFileNm(originalFileName);
                    fileDto.setFilePath(savePath);
                    fileDto.setFileSz((int) file.getSize());
                    fileDto.setFileExtn(fileExtn);

                    atchFileService.insertFileMeta(fileDto);
                }

	        }

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
