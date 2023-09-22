package com.regroup.rsunny.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
	
	@Value("${rsunny.file.upload-path}")
	private String UPLOAD_BASE_PATH;
	
	@Value("${rsunny.file.upload-folder}")
	private String UPLOAD_BASE_FOLDER;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String resourceHandler = String.format("%s/**", UPLOAD_BASE_PATH);
		String resourceLocation = String.format("file:%s%s/", UPLOAD_BASE_FOLDER, UPLOAD_BASE_PATH);
		
		registry.addResourceHandler(resourceHandler)
				.addResourceLocations(resourceLocation);			
	}

}
