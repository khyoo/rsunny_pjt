package com.regroup.rsunny.forsale.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.forsale.model.ReportDTO;
import com.regroup.rsunny.forsale.service.ReportService;
import com.regroup.rsunny.utils.SessionUtil;

import groovyjarjarpicocli.CommandLine.Model;

@RequestMapping("/report/rest")
@RestController
public class ReportRestController { 

	@Autowired
	private ReportService service;

	/**
	 * 신고하기 저장
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/getReport")
	public ReportDTO getReport(ReportDTO form, Model model) throws Exception {
		
		form.setSessionId(SessionUtil.getUserid());
		
		return service.getReport(form);
	}

	/**
	 * 신고하기 저장
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/save")
	public ResultDTO save(ReportDTO form, Model model) throws Exception {
		
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		form.setSessionId(SessionUtil.getUserid());
		
		return service.saveReport(form);
	}

	/**
	 * 신고하기 포인트 반환
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/refund")
	public ResultDTO refund(ReportDTO form, Model model) throws Exception {
		
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		form.setSessionId(SessionUtil.getUserid());
		
		return service.refundPoint(form);
	}

}
