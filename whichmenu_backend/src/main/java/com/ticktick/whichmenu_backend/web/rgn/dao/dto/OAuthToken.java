package com.ticktick.whichmenu_backend.web.rgn.dao.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class OAuthToken {
	
	private Long id;
	
	private String prov;
	
	private String provUsrSn;

	private String accTkn;
	
	private String refTkn;
	
	private String tknType;
	
	private String scope;
	
	private Long expAt;
	
	private Instant regDt = Instant.now();
	
	private Instant mdfcnDt = Instant.now();

}
