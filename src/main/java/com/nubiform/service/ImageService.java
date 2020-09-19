package com.nubiform.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.nubiform.common.CommonUtils;

@Service
public class ImageService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${data.path}")
	private String dataPath;
	
	@Value("${data.thumb-path}")
	private String thumbDataPath;
	
	public ArrayList<String> getDirectorys() {
		File directory = new File(dataPath);
		
		if (!directory.exists()) {
			logger.info("does not exists path: " + directory.getName());
			logger.info("mkdir: " + directory.getName());
			directory.mkdirs();
		}
		
		ArrayList<String> result = null;
		
		result = getFiles(directory);
		
		return result;
	}
	
	private ArrayList<String> getFiles(File directory) {
		ArrayList<String> result = new ArrayList<String>();
		
		seekDirectory(directory, result);
		
		return result;
	}
	
	private void seekDirectory(File directory, ArrayList<String> result) {
		File[] fileList = directory.listFiles();
		
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			
			if (file.isFile()) {
				logger.info("- " + file.getPath());
				result.add(file.getPath());
				
				//printMetaData(file);
				printExifSubIF(file);
			}
			else if (file.isDirectory()) {
				logger.info("d " + file.getPath());
				seekDirectory(file, result);
			}
		}
	}
	
	private void printMetaData(File imageFile) {
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			
			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					logger.info("[{}] - {} = {}", directory.getName(), tag.getTagName(), tag.getDescription());
				}
				if (directory.hasErrors()) {
					for (String error : directory.getErrors()) {
						logger.error("ERROR: {}", error);
					}
				}
			}
		} catch (ImageProcessingException e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
		} catch (IOException e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
		}
	}
	
	private void printExifSubIF(File imageFile) {
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			
			ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			if (directory != null) {
				for (Tag tag : directory.getTags()) {
					logger.info("[{}] - {} = {}", directory.getName(), tag.getTagName(), tag.getDescription());
				}
				if (directory.hasErrors()) {
					for (String error : directory.getErrors()) {
						logger.error("ERROR: {}", error);
					}
				}
			}
		} catch (ImageProcessingException e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
		} catch (IOException e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
		}
	}
	
	public HashMap<Object, HashMap<Object, Object>> getMetadata(String imageName) throws ImageProcessingException, IOException {
		String filePath = dataPath + "/" + imageName;
		logger.info("filePath: {}", filePath);
		
		return getMetadata(ImageMetadataReader.readMetadata(new File(filePath)));
	}
	
	public HashMap<Object, HashMap<Object, Object>> getMetadata(InputStream imageInputStream) throws ImageProcessingException, IOException {
		return getMetadata(ImageMetadataReader.readMetadata(imageInputStream));
	}
	
	public HashMap<Object, HashMap<Object, Object>> getMetadata(Metadata metadata) {
		// return variable
		HashMap<Object, HashMap<Object, Object>> result = new HashMap<Object, HashMap<Object, Object>>();
		
		for (Directory directory : metadata.getDirectories()) {
			if (directory.hasErrors()) {
				for (String error : directory.getErrors()) {
					logger.error("ERROR: {}", error);
				}
			}
			else {
				HashMap<Object, Object> tagMap = new HashMap<Object, Object>();
				for (Tag tag : directory.getTags()) {
					logger.info("[{}] : [{}] = {}", directory.getName(), tag.getTagName(), tag.getDescription());
					tagMap.put(tag.getTagName(), tag.getDescription());
				}
				result.put(directory.getName(), tagMap);
			}
		}
		
		return result;
	}
	
	public HashMap<Object, Object> getMetadata(String imageName, String key) throws ImageProcessingException, IOException {
		return getMetadata(imageName).get(key);
	}
	
	public String getMetadata(String imageName, String key1, String key2) throws ImageProcessingException, IOException {
		return (String)getMetadata(imageName).get(key1).get(key2);
	}
	
	public byte[] getImage(String imageName) {
		String filePath = dataPath + "/" + imageName;
		logger.info("filePath: {}", filePath);
		
		// return variable
		byte[] result = null;
		
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(filePath));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
			result = byteArrayOutputStream.toByteArray();
		}
		catch (Exception e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
			
			logger.info("display no image");
			try {
				InputStream in = getClass().getResourceAsStream("/static/images/no_image.png");
				result = IOUtils.toByteArray(in);
			}
			catch (Exception e1) {
				logger.error(CommonUtils.getPrintStackTrace(e1));
			}
		}
		
		return result;
	}
	
	public byte[] getImageThumb(String imageName) {
		String filePath = dataPath + "/" + imageName;
		String thumbFilePath = thumbDataPath + "/" + imageName;
		String ext = imageName.substring(imageName.lastIndexOf(".") + 1);
		logger.info("filePath: {}", filePath);
		logger.info("thumbFilePath: {}, ext: {}", thumbFilePath, ext);
		
		double baseWidth = 800;
		
		// return variable
		byte[] result = null;
		
		try {
			File thumbFile = new File(thumbFilePath);
			
			if (!thumbFile.exists()) {
				logger.info("create new thumb image");
				BufferedImage bufferedOImage = ImageIO.read(new File(filePath));
				
				int oWidth = bufferedOImage.getWidth();
				int oHeight = bufferedOImage.getHeight();
				
				double ratio = baseWidth / (double)(oWidth > oHeight ? oWidth : oHeight);
				
				int tWidth = (int)(bufferedOImage.getWidth() * ratio);
				int tHeight = (int)(bufferedOImage.getHeight() * ratio);
				
				BufferedImage bufferedTImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR);
				
				Graphics2D graphic = bufferedTImage.createGraphics();
				Image image = bufferedOImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
				graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
				graphic.dispose();
				
				ImageIO.write(bufferedTImage, ext, thumbFile);
			}
			
			BufferedImage bufferedImage = ImageIO.read(thumbFile);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
			result = byteArrayOutputStream.toByteArray();
		}
		catch (Exception e) {
			logger.error(CommonUtils.getPrintStackTrace(e));
			
			logger.info("display no image");
			try {
				InputStream in = getClass().getResourceAsStream("/static/images/no_image.png");
				result = IOUtils.toByteArray(in);
			}
			catch (Exception e1) {
				logger.error(CommonUtils.getPrintStackTrace(e1));
			}
		}
		
		return result;
	}
}