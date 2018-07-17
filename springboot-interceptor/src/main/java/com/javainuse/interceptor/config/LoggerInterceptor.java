package com.javainuse.interceptor.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

	
	Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	private Vani vani = null;
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		//log.error("Request is complete");
		//log.fatal()
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
		//log.error("Handler execution is complete");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		vani = new Vani();
		vani.setUrl(request.getRequestURL().toString());
		System.out.println(request.getRequestURL());
		vani.setServername(request.getServerName());
		System.out.println(request.getServerName());
		vani.setIpaddress(getClientIp(request));
		System.out.println("IP address" + getClientIp(request));
		vani.setModulename("Test");
		vani.setStatus(String.valueOf(response.getStatus()));
		
		
		MDC.put("uri", request.getRequestURL());
		MDC.put("server", request.getServerName());
		//request.setAttribute("vani", vani);
		VaniHelp vaniHelp = new VaniHelp();
		vaniHelp.setVani(vani);
		log.error("Before Handler execution");
		return true;
	}

	private static String getClientIp(HttpServletRequest request) {
		String remoteAddr = "";
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return remoteAddr;
	}

	public Vani getData() {
		return vani;
	}
}