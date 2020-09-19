package com.nubiform.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nubiform.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class ImageController {
	
	private ImageService imageService;
	
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
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
	public HashMap<Object, HashMap<Object, Object>> getMetadata(@RequestParam(value = "id", required=true) String imageName) {
		return imageService.getMetadata(imageName);
	}
	
	@GetMapping("/getMetadataByKey")
	@Operation(description = "get image metadata by key")
	public HashMap<Object, Object> getMetadata(@RequestParam(value = "id", required=true) String imageName, @RequestParam("key") String key) {
		return imageService.getMetadata(imageName, key);
	}
	
	@GetMapping("/getMetadataByKeys")
	@Operation(description = "get image metadata by key1, key2")
	public String getMetadata(@RequestParam(value = "id", required=true) String imageName, @RequestParam("key1") String key1, @RequestParam("key2") String key2) {
		return imageService.getMetadata(imageName, key1, key2);
	}
}