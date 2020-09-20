package com.nubiform.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "images")
public class Images {
	@Id
	private String id;
	private String filename;
	private String originfilename;
	
}