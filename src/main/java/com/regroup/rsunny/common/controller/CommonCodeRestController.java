package com.regroup.rsunny.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.common.model.CodeDTO;
import com.regroup.rsunny.common.service.CommonCodeService;

@RestController
@RequestMapping("/code/rest")
public class CommonCodeRestController { 

	@Autowired
	private CommonCodeService service;

	/**
	 * 코드 목록 조회.
	 * @param CodeDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/code")
	public Map<String, Object> code(CodeDTO form) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<CodeDTO> list = service.getList(form);
		resultMap.put("rows", list);
		
		return resultMap;
	}

	/**
	 * 하위 코드 목록 조회.
	 * @param CodeDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/subCode")
	public Map<String, Object> subCode(CodeDTO form) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<CodeDTO> list = service.getList(form);
		resultMap.put("rows", list);
		
		return resultMap;
	}

	/**
	 * 고객사 목록 조회.
	 * @param CodeDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/custCompany")
	public Map<String, Object> custCompany(CodeDTO form) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<CodeDTO> list = service.getCustCompanyList(form);
		resultMap.put("rows", list);
		
		return resultMap;
	}

	/**
	 * 고객사 담당자 목록 조회.
	 * @param CodeDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/custManager")
	public Map<String, Object> custManager(CodeDTO form) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<CodeDTO> list = service.getCustManagerList(form);
		resultMap.put("rows", list);
		
		return resultMap;
	}

	/**
	 * 담당자 목록 조회.
	 * @param CodeDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/userList")
	public Map<String, Object> users(CodeDTO form) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<CodeDTO> list = service.getUserList();
		resultMap.put("rows", list);
		
		return resultMap;
	}

}
