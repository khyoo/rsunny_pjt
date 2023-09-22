package com.regroup.rsunny.front.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.config.security.Role;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.model.UserDetail;
import com.regroup.rsunny.system.service.UserService;
import com.regroup.rsunny.utils.AES256Util;
import com.regroup.rsunny.utils.SessionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/user")
@RestController
public class FrontUserRestController { 

	@Autowired
	private UserService userService;

	/**
	 * 사용자 로그인 체크.
	 * @param SaleDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/rest/checkLogin")
	public ResultDTO checkLogin(UserDTO form) throws Exception {
		log.info("Check login...{}", form.getUserid());
		return userService.checkLogin(form);
	}

	/**
	 * 사용자 정보 조회
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
		result.setData(AES256Util.encrypt(form.getUserid()));
		
		return result;
	}
	
	/**
	 * 사용자 정보 저장
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/update")
	public ResultDTO updateUser(UserDTO form) throws Exception {
		form.setUserid(SessionUtil.getUserid());	//사용자 아이디는 세션에서 가져다 설정.
		form.setSessionId(SessionUtil.getUserid());
		
		ResultDTO result =  userService.saveUser(form);
		//사용자 연락처나 이메일 변경 내용을 세션에 반영함.
		if(result.getCode()==0) {
			UserDTO user = userService.getUser(form);

            //권한
            List<GrantedAuthority> authorities = new ArrayList<>();
            if("SU".equals(user.getUserType()) || "RM".equals(user.getUserType())) {	//슈퍼관리자 및 알써니관리자만...
                authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }
            else {
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }
            UserDetail authUser = new UserDetail(user.getUserid(), user.getPasswd(), authorities, user.getUsernm(), user.getUserType(), user.getPhone(), user.getEmail(), user.getPoint());
            
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authUser, null, authorities);

            HttpServletRequest request = SessionUtil.getCurrentRequest();
            token.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
		}
		
		return result;
	}
	
	/**
	 * 사용자 정보 저장
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/update-agent")
	public ResultDTO updateAgentUser(UserDTO form) throws Exception {
		form.setUserid(SessionUtil.getUserid());	//사용자 아이디는 세션에서 가져다 설정.
		form.setSessionId(SessionUtil.getUserid());
		
		ResultDTO result =  userService.updateAgentUser(form);
		//사용자 연락처나 이메일 변경 내용을 세션에 반영함.
		if(result.getCode()==0) {
			UserDTO user = userService.getUser(form);

            //권한
            List<GrantedAuthority> authorities = new ArrayList<>();
            if("SU".equals(user.getUserType()) || "RM".equals(user.getUserType())) {	//슈퍼관리자 및 알써니관리자만...
                authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }
            else {
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }
            UserDetail authUser = new UserDetail(user.getUserid(), user.getPasswd(), authorities, user.getUsernm(), user.getUserType(), user.getPhone(), user.getEmail(), user.getPoint());
            
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authUser, null, authorities);

            HttpServletRequest request = SessionUtil.getCurrentRequest();
            token.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
		}
		
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
	 * 사용자 아이디/비밀번호 찾기.
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/findUser")
	public ResultDTO findUser(UserDTO form) throws Exception {
		return userService.findUser(form);
	}
	
	/**
	 * 사용자 비밀번호 변경.
	 * @param SaleDTO
	 * @return List<UserDTO>
	 * @throws Exception
	 */
	@PostMapping("/rest/changePasswd")
	public ResultDTO changePasswd(UserDTO form) throws Exception {
		form.setUserid(AES256Util.decrypt(form.getUserToken()));
		log.info("Change passwd...userid={}", form.getUserid());
		return userService.changePasswd(form);
	}

}
