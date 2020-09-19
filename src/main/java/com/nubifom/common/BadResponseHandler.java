package com.nubifom.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class BadResponseHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler({
		NoHandlerFoundException.class,
		MissingServletRequestParameterException.class,
		MissingRequestCookieException.class,
		MethodArgumentTypeMismatchException.class,
		BindException.class})
	public String noHandlerFoundException(HttpRequest req, HttpServletBean res, Exception e) {
		logger.error(e.getMessage());
		return e.getMessage();
	}
	
	@ExceptionHandler(Exception.class)
	public String exception(HttpRequest req, HttpServletBean res, Exception e) {
		logger.error(e.getMessage());
		return e.getMessage();
	}
	
	@ExceptionHandler(Throwable.class)
	public String throwable(HttpRequest req, HttpServletBean res, Exception e) {
		logger.error(e.getMessage());
		return e.getMessage();
	}
}