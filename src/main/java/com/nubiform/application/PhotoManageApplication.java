package com.nubiform.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.nubiform")
@MapperScan("com.nubiform.dao")
public class PhotoManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoManageApplication.class, args);
	}

}
