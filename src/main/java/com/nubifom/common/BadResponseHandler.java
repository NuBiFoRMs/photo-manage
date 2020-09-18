package com.nubifom.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class BadResponseHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public String noHandlerFoundException(HttpRequest req, HttpServletBean res, Exception e) {
		logger.error(e.getMessage());
		return e.getMessage();
	}
	
	@ExceptionHandler(Exception.class)
	public String exception(HttpRequest req, HttpServletBean res, Exception e) {
		logger.error(e.getMessage());
		return e.getMessage();
	}
}