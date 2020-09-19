package com.nubiform.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.nubiform.common.CommonUtils;

@Service
public class ImageUploadService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${data.path}")
	private String dataPath;
	
	@Value("${data.thumb-path}")
	private String thumbDataPath;
	
	private ImageService imageService;
	
	public ImageUploadService(ImageService imageService) {
		this.imageService = imageService;
	}
	
	public HashMap<Object, HashMap<Object, Object>> uploadImage(String fileName, byte[] imageData) {
		UUID uuid = UUID.randomUUID();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		String uuidFileName = dataPath + "/" + uuid + "." + ext;
		logger.info(uuidFileName);
		
		HashMap<Object, HashMap<Object, Object>> metadata = new HashMap<Object, HashMap<Object, Object>>();
		
		try {
			FileCopyUtils.copy(imageData, new File(uuidFileName));
			metadata = imageService.getMetadata(new ByteArrayInputStream(imageData));
		}
		catch (Exception e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
		}
		
		return metadata;
	}
}
