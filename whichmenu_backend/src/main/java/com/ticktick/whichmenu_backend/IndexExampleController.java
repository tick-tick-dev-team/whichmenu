package com.ticktick.whichmenu_backend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ticktick.whichmenu_backend.web.mmgr.service.MngrInfoService;

/**
 * 임시용으로 만들어둔 홈 controller
 * http://localhost:8080/ 로 접속하면 home() 메소드 호출
 * 
 * */

@Controller
public class IndexExampleController {
	
	private final MngrInfoService mngrInfoService;
	
	public IndexExampleController(@Qualifier("mngrInfoServiceImpl") MngrInfoService mngrInfoService) {
		this.mngrInfoService = mngrInfoService;
	}

	@GetMapping("/")
	public String home() {
		mngrInfoService.selectAllMngrs();
		return "index";
	}
}
