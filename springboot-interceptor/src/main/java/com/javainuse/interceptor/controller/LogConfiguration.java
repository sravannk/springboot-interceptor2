/*package com.javainuse.interceptor.controller;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.qos.logback.classic.LoggerContext;


@Configuration
public class LogConfiguration {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	private void configLogback() {
		for (String env1 :env) {
	        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();\
	        ClassPathResource resource = new ClassPathResource("logback-" +profile+ ".xml");
	        try {
	        	loggerContext.set
	        }

		}
	}
	

	@RequestMapping("/logger")
	public String executeLogger() {
		log.info("inside the executeLogger method");
		return "Hello World Logger Interceptor";
	}
}*/