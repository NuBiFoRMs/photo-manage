package com.nubiform.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nubiform.service.ImageService;

@RestController
public class ImageController {
	
	@Autowired
	ImageService imageService;
	
	@RequestMapping("/getDirectories")
	public ArrayList<String> getDirectorys() {
		return imageService.getDirectorys();
	}
	
	@RequestMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImage(@RequestParam("id") String imageName) {
		return imageService.getImage(imageName);
	}
	
	@RequestMapping(value = "/getImageThumb", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImageThumb(@RequestParam("id") String imageName) {
		return imageService.getImageThumb(imageName);
	}
	
	@RequestMapping("/getMetadata")
	public HashMap<Object, HashMap<Object, Object>> getMetadata(@RequestParam(value = "id", required=true) String imageName) {
		return imageService.getMetadata(imageName);
	}
	
	@RequestMapping("/getMetadataByKey")
	public HashMap<Object, Object> getMetadata(@RequestParam(value = "id", required=true) String imageName, @RequestParam("key") String key) {
		return imageService.getMetadata(imageName, key);
	}
	
	@RequestMapping("/getMetadataByKeys")
	public String getMetadata(@RequestParam(value = "id", required=true) String imageName, @RequestParam("key1") String key1, @RequestParam("key2") String key2) {
		return imageService.getMetadata(imageName, key1, key2);
	}
}