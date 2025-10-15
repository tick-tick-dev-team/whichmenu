package com.ticktick.whichmenu_backend.web.rgn.dao.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class User {

	private Long id;
	
	private String username;
	
	private String displayName;
	
	private String email;
	
	private Instant createdAt;
}
