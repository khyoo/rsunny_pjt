package com.regroup.rsunny.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.service.UserService;
import com.regroup.rsunny.utils.SessionUtil;

@RequestMapping("/siteadmin/user")
@RestController
public class UserRestController { 

	@Autowired
	private UserService userService;

	/**
	 * 사용자 로그인 체크.
	 * @param SaleDTO
	 * @return UserDTO
	 * @throws Exception
	 */
	@PostMapping("/rest/getUser")
	public UserDTO getUser(UserDTO form) throws Exception {
		return userService.getUser(form);
	}
	
	/**
	 * 사용자 정보 저장
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/save")
	public ResultDTO saveUser(UserDTO form) throws Exception {
		form.setSessionId(SessionUtil.getUserid());
		
		ResultDTO result =  userService.saveUser(form);
		
		return result;
	}
	
	/**
	 * 사용자 정보 삭제
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/delete")
	public ResultDTO deleteUser(UserDTO form) throws Exception {
		form.setSessionId(SessionUtil.getUserid());
		
		ResultDTO result =  userService.deleteUser(form);
		
		return result;
	}
	
	/**
	 * 사용자 승인
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/approve")
	public ResultDTO approve(UserDTO form) throws Exception {
		form.setSessionId(SessionUtil.getUserid());
		
		ResultDTO result =  userService.approveUser(form);
		
		return result;
	}
	
	/**
	 * 사용자 사용정지
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/stop")
	public ResultDTO stop(UserDTO form) throws Exception {
		form.setSessionId(SessionUtil.getUserid());
		
		ResultDTO result =  userService.stopUser(form);
		
		return result;
	}
	
	/**
	 * 사용자 사용정지
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/usertype")
	public ResultDTO usertype(UserDTO form) throws Exception {
		form.setSessionId(SessionUtil.getUserid());
		form.setUserType("BM");
		ResultDTO result =  userService.changeUserType(form);
		
		return result;
	}

}
