package com.ticktick.whichmenu_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ticktick.whichmenu_backend.web")
public class WhichmenuBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhichmenuBackendApplication.class, args);
	}

}
