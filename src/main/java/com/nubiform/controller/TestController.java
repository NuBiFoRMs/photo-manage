package com.nubiform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nubiform.mongo.document.Images;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@GetMapping("/test")
	public List<Images> test() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.DESC, "shoottime"));
		List<Images> result = mongoTemplate.find(query, Images.class);
		
		for (Images images : result) {
			logger.info("{} {} {} {}", images.getId(), images.getFilename(), images.getOriginfilename(), images.getShoottime());
		}
		
		return result;
	}

}
