package com.ticktick.whichmenu_backend.web.rest.service;

import java.util.List;

import com.ticktick.whichmenu_backend.web.rest.dao.dto.RestInfoDto;

/**
 * 식당 Service
 * 
 * */

public interface RestInfoService {
	
	public List<RestInfoDto> selectRestList(RestInfoDto restInfoDto);
	
	public RestInfoDto selectOneRest(RestInfoDto restInfoDto);
	
	public void insertRestInfo(RestInfoDto restInfoDto);
	
	public void updateRestInfo(RestInfoDto restInfoDto);
	
	public void deleteRestInfo(RestInfoDto restInfoDto);

}
