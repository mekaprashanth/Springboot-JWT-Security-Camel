package com.prash.spring.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String tokenPrefix = "";
    
	private String headerString;

	private JwtTokenUtil jwtTokenUtil;
	
	public JwtLoginFilter(JwtTokenUtil jwtTokenUtil, String headerString, String url, String method, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(url, method, true));
		setAuthenticationManager(authenticationManager);
		this.jwtTokenUtil = jwtTokenUtil;
		this.headerString = headerString;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		String username = httpServletRequest.getParameter("username");
		String password = httpServletRequest.getParameter("password");
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		logger.info("attempting authentication using "+username);
		return getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		String token = jwtTokenUtil.generateToken(authentication);
		logger.info("Adding Authorization Header in response "+(headerString+" - "+tokenPrefix + " " + token));
		response.addHeader(headerString, tokenPrefix + token);
	}
}