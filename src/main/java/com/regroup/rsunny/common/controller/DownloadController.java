package com.regroup.rsunny.common.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.regroup.rsunny.common.model.FileDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DownloadController {

	@Autowired 
	private ServletContext context;

	/* 파일 다운로드 */
	@RequestMapping("/download.do")
	@ResponseBody
	public void download(FileDTO form, HttpServletRequest request, HttpServletResponse response) {
		log.info("Download");
	}
	
}
