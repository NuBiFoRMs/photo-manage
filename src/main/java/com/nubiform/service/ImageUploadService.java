package com.nubiform.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.nubiform.mongo.document.Images;

@Service
public class ImageUploadService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${data.path}")
	private String dataPath;
	
	@Value("${data.thumb-path}")
	private String thumbDataPath;
	
	private MongoTemplate mongoTemplate;
	
	public ImageUploadService(MongoTemplate mongoTemplate, ImageService imageService) {
		this.mongoTemplate = mongoTemplate;
	}
	
	public File uploadImage(String fileName, InputStream imageInputStream) throws IOException {
		UUID uuid = UUID.randomUUID();
		String ext = FilenameUtils.getExtension(fileName);
		File uuidFile = null;
		if (!"".equals(ext) && ext != null) {
			uuidFile = FileUtils.getFile(dataPath, uuid + "");
		}
		else {
			uuidFile = FileUtils.getFile(dataPath, uuid + "." + ext);
		}
		logger.info(uuidFile.getPath());
		
		FileUtils.copyToFile(imageInputStream, uuidFile);
		
		return uuidFile;
	}
	
	public void insertImages(String filename, String originFileName, HashMap<Object, HashMap<Object, Object>> metadata) {
		Images images = new Images();
		images.setFilename(filename);
		images.setOriginfilename(originFileName);
		images.setMetadata(metadata);
		mongoTemplate.insert(images);
	}
}
