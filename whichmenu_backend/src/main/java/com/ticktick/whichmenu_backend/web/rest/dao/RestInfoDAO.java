package com.ticktick.whichmenu_backend.web.rest.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ticktick.whichmenu_backend.web.rest.dao.dto.RestInfoDto;

@Mapper
public interface RestInfoDAO {
	
	List<RestInfoDto> selectRestList(RestInfoDto restInfoDto);
	
	RestInfoDto selectOneRest(RestInfoDto restInfoDto);
	
	void insertRestInfo(RestInfoDto restInfoDto);
	
	void updateRestInfo(RestInfoDto restInfoDto);
	
	void deleteRestInfo(RestInfoDto restInfoDto);

}
