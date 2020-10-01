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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ImageUploadService {
	
	@Value("${data.path}")
	private String dataPath;
	
	@Value("${data.thumb-path}")
	private String thumbDataPath;
	
	private final MongoTemplate mongoTemplate;
	
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
		Images images = Images.builder()
		.filename(filename)
		.originfilename(originFileName)
		.metadata(metadata)
		.shoottime(shoottime)
		.uploadtime(uploadtime)
		.build();
		
		mongoTemplate.insert(images);
	}
	
}
