package com.ticktick.whichmenu_backend.web.rgn.dao.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class OAuthToken {
	
	private Long id;
	
	private String provider;
	
	private String providerUserId;

	private String accessToken;
	
	private String refreshToken;
	
	private String tokenType;
	
	private String scope;
	
	private Long expiresAt;
	
	private Instant createdAt = Instant.now();
	
	private Instant updatedAt = Instant.now();

}
