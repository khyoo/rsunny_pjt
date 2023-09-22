package com.regroup.rsunny.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/siteadmin")
@RestController
public class AdminRestController { 

	@Autowired
	private UserService service;

	/**
	 * 관리자 로그인 체크.
	 * @param SaleDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/rest/checkLogin")
	public ResultDTO checkLogin(UserDTO form) throws Exception {
		log.info("Check login...{}", form.getUserid());
		return service.checkLogin(form);
	}

}
