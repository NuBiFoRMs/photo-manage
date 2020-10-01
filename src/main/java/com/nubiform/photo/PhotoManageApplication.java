package com.nubiform.photo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.nubiform")
public class PhotoManageApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PhotoManageApplication.class, args);
	}
	
}