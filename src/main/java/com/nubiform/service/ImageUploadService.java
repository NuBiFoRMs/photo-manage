package com.nubiform.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.nubiform.mongo.document.Images;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageUploadService {
	
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
			uuidFile = FileUtils.getFile(dataPath, uuid + "." + ext);
		}
		else {
			uuidFile = FileUtils.getFile(dataPath, uuid + "");
		}
		logger.info(uuidFile.getPath());
		
		FileUtils.copyToFile(imageInputStream, uuidFile);
		
		return uuidFile;
	}
	
	public void insertImages(String filename, String originFileName, HashMap<Object, HashMap<Object, Object>> metadata, long shoottime, long uploadtime) {
		Images images = new Images();
		images.setFilename(filename);
		images.setOriginfilename(originFileName);
		images.setMetadata(metadata);
		images.setShoottime(shoottime);
		images.setUploadtime(uploadtime);
		mongoTemplate.insert(images);
	}
}
