package com.nubiform.photo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageProcessingException;
import com.nubiform.photo.common.CommonUtils;
import com.nubiform.photo.mongo.document.Images;
import com.nubiform.photo.service.ImageService;
import com.nubiform.photo.service.ImageUploadService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {
	
	private final ImageService imageService;
	
	private final ImageUploadService imageUploadService;
	
	@GetMapping("/getImageList")
	public List<Images> getImageList() {
		return imageService.getImageList();
	}
	
	@GetMapping("/getDirectories")
	public ArrayList<String> getDirectorys() {
		return imageService.getDirectorys();
	}
	
	@GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
	@Operation(description = "get image")
	public byte[] getImage(@RequestParam("id") String imageName) {
		return imageService.getImage(imageName);
	}
	
	@GetMapping(value = "/getImageThumb", produces = MediaType.IMAGE_JPEG_VALUE)
	@Operation(description = "get thumbnail image")
	public byte[] getImageThumb(@RequestParam("id") String imageName) {
		return imageService.getImageThumb(imageName);
	}
	
	@GetMapping("/getMetadata")
	@Operation(description = "get image metadata")
	public HashMap<Object, HashMap<Object, Object>> getMetadata(@RequestParam(value = "id", required=true) String imageName) throws ImageProcessingException, IOException {
		return imageService.getMetadata(imageName);
	}
	
	@GetMapping("/getMetadataByKey")
	@Operation(description = "get image metadata by key")
	public HashMap<Object, Object> getMetadata(@RequestParam(value = "id", required=true) String imageName, @RequestParam("key") String key) throws ImageProcessingException, IOException {
		return imageService.getMetadata(imageName, key);
	}
	
	@GetMapping("/getMetadataByKeys")
	@Operation(description = "get image metadata by key1, key2")
	public String getMetadata(@RequestParam(value = "id", required=true) String imageName, @RequestParam("key1") String key1, @RequestParam("key2") String key2) throws ImageProcessingException, IOException {
		return imageService.getMetadata(imageName, key1, key2);
	}
	
	@PostMapping("/uploadImage")
	public HashMap<Object, HashMap<Object, Object>> uploadImage(@RequestParam(value="filename") MultipartFile imageFile) {
		logger.info("{} {} {}", imageFile.getOriginalFilename(), imageFile.getContentType(), imageFile.getSize());
		
		HashMap<Object, HashMap<Object, Object>> metadata = null;
		
		try {
			File uploadFile = imageUploadService.uploadImage(imageFile.getOriginalFilename(), imageFile.getInputStream());
			metadata = imageService.getMetadata(imageFile.getInputStream());
			Date datetime = imageService.getMetadataDate(imageFile.getInputStream());
			imageUploadService.insertImages(uploadFile.getName(), imageFile.getOriginalFilename(), metadata, datetime.getTime(), System.currentTimeMillis());
		}
		catch (Exception e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
		}
		
		return metadata;
	}
	
}