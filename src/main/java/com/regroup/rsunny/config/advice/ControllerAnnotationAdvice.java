package com.regroup.rsunny.config.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.service.UserService;
import com.regroup.rsunny.utils.SessionUtil;

@ControllerAdvice(annotations = {Controller.class, RestController.class})
//@ControllerAdvice(assignableTypes = {DefaultController.class, OrderController.class, BoardController.class})
public class ControllerAnnotationAdvice {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	HttpServletRequest request;

    @ModelAttribute("IS_AUTHENTICATED")
    public boolean isAuthenticated() {
		return SessionUtil.isAuthenticated();
    }

    @ModelAttribute("USER_ID")
    public String userid() {
		return SessionUtil.getUserid();
    }

    @ModelAttribute("USER_TYPE")
    public String userType() {
		return SessionUtil.getUserType();
    }

    @ModelAttribute("USER_NM")
    public String usernm() {
		return SessionUtil.getUsernm();
    }

    @ModelAttribute("IS_AGENT_USER")
    public boolean isAgentUser() {
		return SessionUtil.isAgentUser();
    }

    @ModelAttribute("USER_POINT")
    public long userPoint() {
		return SessionUtil.getPoint();
    }

    @ModelAttribute("USER_POINT_WITH_COMMA")
    public String userPointWithComma() {
		return String.format("%,d", SessionUtil.getPoint());
    }

    @ModelAttribute("USE_AVAIL_POINT")
    public Long useAvailPoint() {
    	UserDTO user = userService.getUser(UserDTO.of(SessionUtil.getUserid()));
		return (user==null)? 0 : user.getUseAvailPoint();
    }

    @ModelAttribute("USE_AVAIL_ITEM")
    public Long useAvailItem() {
    	UserDTO user = userService.getUser(UserDTO.of(SessionUtil.getUserid()));
		return (user==null)? 0 : user.getUseAvailItem();
    }

    @ModelAttribute("USER_PHONE")
    public String userPhone() {
    	String phone = SessionUtil.getPhone().replaceAll("-", "");
    	if(phone.length()==11) {
    		return String.format("%s-%s-%s", phone.substring(0,3), phone.substring(3,7), phone.substring(7,11));
    	}
    	else if(phone.startsWith("02") && phone.length()==10) {
    		return String.format("%s-%s-%s", phone.substring(0,2), phone.substring(2,6), phone.substring(6,10));
    	}
    	else if(phone.length()==10) {
    		return String.format("%s-%s-%s", phone.substring(0,3), phone.substring(3,6), phone.substring(6,10));
    	}
    	else {
    		return phone;
    	}
    }

    @ModelAttribute("USER_EMAIL")
    public String userEmail() {
		return SessionUtil.getEmail();
    }

    @ModelAttribute("CURRENT_PAGE")
    public String currentPage() {
    	String url = request.getRequestURL().toString();
    	String page = url.substring(url.lastIndexOf("/"));
    	return page;
    }

}
