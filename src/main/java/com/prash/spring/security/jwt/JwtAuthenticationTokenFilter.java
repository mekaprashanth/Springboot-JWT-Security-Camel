package com.prash.spring.security.jwt;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JwtTokenUtil jwtTokenUtil;

    private String tokenHeader;
    
    public JwtAuthenticationTokenFilter(JwtTokenUtil jwtTokenUtil, String tokenHeader) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.tokenHeader = tokenHeader;
	}
    

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    	
        String authToken = ((HttpServletRequest)request).getHeader(this.tokenHeader);
    	logger.info("Token Header "+ this.tokenHeader+" with token "+authToken);
    	if(authToken != null && SecurityContextHolder.getContext().getAuthentication() == null)	{
    		String username = jwtTokenUtil.getUsernameFromToken(authToken);
            boolean isTokenExpired = jwtTokenUtil.isTokenExpired(authToken);

            logger.info("checking authentication for user " + username);

            if (username != null && !isTokenExpired) {
                Collection<GrantedAuthority> roles = jwtTokenUtil.getAuthorities(authToken);
                JwtUser authenticatedUser = createUserFromToken(username, isTokenExpired, roles);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest)request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
    	}
        

        chain.doFilter(request, response);
    }

	private JwtUser createUserFromToken(String username, boolean isTokenExpired, Collection<GrantedAuthority> roles) {
		JwtUser authenticatedUser = new JwtUser(null, username, null, null, null, null, roles, isTokenExpired, null);
		return authenticatedUser;
	}
}