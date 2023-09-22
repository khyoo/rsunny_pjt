package com.regroup.rsunny.system.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetail extends User{
	private static final long serialVersionUID = -8783965385224510721L;
	
	private String userid;
	private String usernm;
	private String userType;
	private String phone;
	private String email;
	private String logintime;
	private long point;
	
	public UserDetail(String userid, String password, Collection<? extends GrantedAuthority> authorities) {
	    super(userid, password, authorities);
	}
	
	public UserDetail(String userid, String passwd, Collection<? extends GrantedAuthority> authorities, String usernm, String userType, String phone, String email, long point) {
	    super(userid, passwd, authorities);
	    
	    this.userid = userid;
	    this.usernm = usernm;
	    this.userType = userType;
	    this.phone = phone;
	    this.email = email;
	    this.point = point;
	    this.logintime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}
	
	public UserDetail getUser() {
	    return this;
	}
	
}
