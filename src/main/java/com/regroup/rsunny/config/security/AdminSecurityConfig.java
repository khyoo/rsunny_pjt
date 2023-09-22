package com.regroup.rsunny.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.regroup.rsunny.system.service.impl.UserServiceImpl;

@Configuration
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserServiceImpl userService;

	@Autowired
    private PasswordEncoder passwordEncoder;

	//@Autowired
    //private SpringSecurityDialect springSecurityDialect;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
        .ignoring()
        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/img/**", "/vendor/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	
        http
        	.authorizeRequests()
            	.antMatchers("/enc/**").permitAll()
            	.antMatchers("/siteadmin/rest/checkLogin").permitAll()
                .antMatchers("/siteadmin/**").hasRole("ADMIN")
                //.antMatchers("/**").authenticated()
                .antMatchers("/**").permitAll()
            .and()
                .formLogin()
                .usernameParameter("userid")
                .passwordParameter("passwd")
                .loginPage("/siteadmin/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/siteadmin/login?error=true")
                .permitAll()
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/siteadmin/logout"))
                .logoutSuccessUrl("/siteadmin/login")
                .invalidateHttpSession(true)
        	.and()
                .exceptionHandling().accessDeniedPage("/siteadmin/login");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}

