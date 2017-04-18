package com.prash.spring.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prash.spring.security.jwt.JwtAuthenticationTokenFilter;
import com.prash.spring.security.jwt.JwtLoginFilter;
import com.prash.spring.security.jwt.JwtTokenUtil;

@EnableWebSecurity()
public class MultiHttpSecurityConfig {

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		AuthenticationManager authenticationManager;
		
		@Autowired
		private JwtTokenUtil jwtTokenUtil;

		@Value("${jwt.header}")
		private String tokenHeader;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.parentAuthenticationManager(authenticationManager);
		}

		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/**/api/**").authorizeRequests()
			.antMatchers("/**/api/date").permitAll()
			.antMatchers("/**/api/admin/**").hasRole("ADMIN")
			.antMatchers("/**/api/**").hasRole("USER")
			.anyRequest().authenticated().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilterBefore(new JwtAuthenticationTokenFilter(jwtTokenUtil, tokenHeader),
					UsernamePasswordAuthenticationFilter.class)
			.httpBasic().disable()
			.csrf().disable();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**").antMatchers("/api/date");
		}
	}

	@Configuration
	@Order(2)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private JwtTokenUtil jwtTokenUtil;

		@Value("${jwt.header}")
		private String tokenHeader;

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("user").password("user").roles("USER").and().withUser("admin")
					.password("admin").roles("USER", "ADMIN");

		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.headers().frameOptions().sameOrigin().and().csrf().disable()
					.authorizeRequests().antMatchers("/error").permitAll().anyRequest().authenticated().and()
					.formLogin().loginProcessingUrl("/login").failureUrl("/login?error").permitAll().and().logout().permitAll()
					.and()
					.addFilterBefore(new JwtLoginFilter(jwtTokenUtil, tokenHeader, "/login", "POST", authenticationManager()),
							UsernamePasswordAuthenticationFilter.class)
					
					;
		}

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManager();
		}
	}
}