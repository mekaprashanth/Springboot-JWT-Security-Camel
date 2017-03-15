package com.prash.spring.controller;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prash.spring.beans.User;

@RestController
public class SampleController {
	

	@RequestMapping("/api/date")
	public Date thing() {
		return new Date();
	}

	@RequestMapping("/api/user")
	public User user() {
		User user = new User();
		user.setFirstName("Prashanth");
		user.setLastName("M");
		user.setAge(34);
		return user;
	}
	
	@RequestMapping("/api/admin/authuser")
	public Authentication calendar(@AuthenticationPrincipal Authentication authentication) {
		return authentication;
	}
}