package com.ticktick.whichmenu_backend.web.atch.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ticktick.whichmenu_backend.web.atch.dao.dto.AtchFileDto;
import com.ticktick.whichmenu_backend.web.atch.service.AtchFileService;

/**
 * 첨부파일 Controller
 * 
 * */

@RestController
@RequestMapping("/api/file")
public class AtchFileController {
	
	private final AtchFileService atchFileService;
	
	// application.properties에서 값 주입받음
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	public AtchFileController(@Qualifier("atchFileServiceImpl") AtchFileService atchFileService) {
		this.atchFileService = atchFileService;
	}
	
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, 
			@RequestParam("atchReferId") String atchReferId,
			@RequestParam("refType") String refType) throws IOException {
		
		if(file.isEmpty()) {
			return "파일이 비어 있습니다.";
		}
		
		//  파일명까지 포함된 “전체 파일 경로” 생성
		String originalFileName = file.getOriginalFilename();
		String fileExtn = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
		// 중복이 없도록 생성
		String savePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + originalFileName;
		
		File dest = new File(savePath);
		dest.getParentFile().mkdirs(); // 디렉토리 없으면 생성
		file.transferTo(dest); // 실제 파일 이동
		
		// DB에 메타데이터 저장
		AtchFileDto fileDto = new AtchFileDto();
        fileDto.setAtchReferId(atchReferId);
        fileDto.setFileOdr(1); // 기본 1로 설정
        fileDto.setFileNm(originalFileName);
        fileDto.setFilePath(savePath);
        fileDto.setFileSz((int) file.getSize());
        fileDto.setFileExtn(fileExtn);
        fileDto.setRefType(refType);
        
        // DB에 저장하는 로직
        atchFileService.insertFileMeta(fileDto);
		
		
		return "업로드 성공!";
	}
	
	@GetMapping("/list/{atchReferId}")
	public List<AtchFileDto> getFileList(@PathVariable("atchReferId") int atchReferId) {
	    return atchFileService.getFilesByReferId(atchReferId);
	}

}
