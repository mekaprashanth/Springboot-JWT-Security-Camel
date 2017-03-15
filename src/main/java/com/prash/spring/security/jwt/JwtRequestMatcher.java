package com.prash.spring.security.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtRequestMatcher implements RequestMatcher {
	public boolean matches(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		boolean haveAuthToken = (auth != null) && auth.startsWith("Bearer");
		return haveAuthToken;
	}
}