package com.javainuse.interceptor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/logger")
	public String executeLogger() {
		log.info("inside the executeLogger method");
		return "Hello World Logger Interceptor";
	}
}