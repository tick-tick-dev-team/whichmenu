package com.ticktick.whichmenu_backend.web.mmgr.dao.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 정보 DTO
 * 
 * */
@Data
public class MngrInfoDto {
	
	private int mngrNo;			// 관리자번호
	private String mngrNm;		// 관리자명
	private String mngrPw;		// 관리자비밀번호
	
	@Override
	public String toString() {
		return "mngrNo=" + mngrNo + ", mngrNm=" + mngrNm + ", mngrPw=" + mngrPw + " ";
	}
	
	
}
