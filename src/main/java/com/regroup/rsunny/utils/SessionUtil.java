package com.regroup.rsunny.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.regroup.rsunny.system.model.UserDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SessionUtil {

    public static boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetail user = null;
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return (user != null && "SU".equals(user.getUserType()));
    }

    public static String getUserid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetail user = null;
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return (user == null)? null : user.getUserid();
    }
    
    public static HttpServletRequest getCurrentRequest() {
    	return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated());
    }

    public static long getPoint() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetail user = null;
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return (user == null)? 0 : user.getPoint();
    }

    public static String getPhone() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetail user = null;
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return (user == null || StringUtils.isEmpty(user.getPhone()))? "" : user.getPhone();
    }

    public static String getEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetail user = null;
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return (user == null || StringUtils.isEmpty(user.getEmail()))? "" : user.getEmail();
    }

    public static String getUsernm() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetail user = null;
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return (user == null || StringUtils.isEmpty(user.getUsernm()))? "" : user.getUsernm();
    }

    public static String getUserType() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetail user = null;
		if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
			user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return (user == null || StringUtils.isEmpty(user.getUserType()))? "" : user.getUserType();
    }

    public static boolean isAgentUser() {
		String userType = getUserType();
		
		return ("AA".equals(userType) || "RA".equals(userType));	//공인중개사(AA) 또는 임대관리자(RA)인지 여부를 리턴.
    }

    public static String getDomainUrl() {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String domainUrl = String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort());
		try {
			URL url = new URL(domainUrl);
			log.info("domainUrl: {}... url: {}", domainUrl, url.toString());
		    return url.toString();
		}
		catch(MalformedURLException e) {
			log.error("[ERROR] {}", e);
			return "";
		}
    }

}
