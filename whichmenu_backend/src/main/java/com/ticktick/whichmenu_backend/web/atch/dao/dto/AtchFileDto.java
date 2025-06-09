package com.ticktick.whichmenu_backend.web.atch.dao.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 첨부파일 Dto
 * 
 * */

@Data
public class AtchFileDto {
	
	// 첨부파일 ID (PK)
	private int atchFileId;

	// 참조 ID (게시글 등과 연결되는 그룹 키)
	private int atchReferId;

	// 파일 순서 (여러 개 첨부 시 정렬용)
	private int fileOdr;

	// 실제 파일명 (사용자 업로드한 원본 이름)
	private String fileNm;

	// 서버에 저장된 파일 경로 (전체 경로)
	private String filePath;

	// 파일 크기 (byte 단위)
	private int fileSz;

	// 파일 확장자 (예: jpg, png, pdf)
	private String fileExtn;

	// 참조 유형 (P: 일반게시판, M: 메뉴 등 구분용)
	private String refType;
	

}
