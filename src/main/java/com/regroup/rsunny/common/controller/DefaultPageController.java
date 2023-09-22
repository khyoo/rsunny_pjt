package com.regroup.rsunny.common.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DefaultPageController { 
	
    @GetMapping("/enc/{passwd}")
    public String enc(@PathVariable String passwd, Model model) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		model.addAttribute("passwd", passwd);
		model.addAttribute("enc", encoder.encode(passwd));

        return "encpassword";
    }
    
    @GetMapping("/admin/{page}")
    public String admin(@PathVariable String page, Model model) {

        return String.format("admin/%s", page);
    }

}
