package com.prash.spring.controller;

import static com.prash.spring.beans.validators.UserValidation.checkAgeNSalary;
import static com.prash.spring.beans.validators.UserValidation.firstNameIsNotEmpty;
import static com.prash.spring.beans.validators.UserValidation.lastNameIsNotEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prash.spring.beans.User;
import com.prash.spring.beans.validators.ErrorEnum;
import com.prash.spring.beans.validators.UserValidation;
import com.prash.spring.beans.validators.ValidationResult;

@RestController
public class SampleController {
	

	@RequestMapping("/api/date")
	public Date thing() {
		return new Date();
	}

	@GetMapping("/api/user")
	public User user() {
		User user = new User();
		user.setFirstName("Prashanth");
		user.setLastName("M");
		user.setAge(34);
		return user;
	}
	
	@PostMapping(value="/v1/api/user", consumes="application/json", produces="application/json")
	public User save(@RequestBody User user) {
		List<ErrorEnum> results = UserValidation.allv2(user, firstNameIsNotEmpty(),lastNameIsNotEmpty(),checkAgeNSalary());
		results.stream().forEach(System.out::println);
		if(results.size() > 0)	{
			throw new ConstraintViolationException(null);
		}
		return user;
	}
	
	@PostMapping(value="/v2/api/user", consumes="application/json", produces="application/json")
	public User savev2(@RequestBody User user) {
		List<ErrorEnum> results = new ArrayList<>();
		UserValidation validation = UserValidation.all(results, firstNameIsNotEmpty(),lastNameIsNotEmpty(),checkAgeNSalary());
		validation.apply(user);
		results.stream().forEach(System.out::println);
		if(results.size() > 0)	{
			throw new ConstraintViolationException(null);
		}
		return user;
	}
	
	@RequestMapping("/api/admin/authuser")
	public Authentication calendar(@AuthenticationPrincipal Authentication authentication) {
		return authentication;
	}
}