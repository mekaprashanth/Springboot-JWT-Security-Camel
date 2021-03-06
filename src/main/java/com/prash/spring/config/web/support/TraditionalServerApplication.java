package com.prash.spring.config.web.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.prash.spring.quartz.SchedulerConfiguration;

@SpringBootApplication
@ComponentScan("com.prash.spring")
@Import({SchedulerConfiguration.class})
public class TraditionalServerApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		 return builder.sources(TraditionalServerApplication.class);
    }

    public static void main(String[] args) {
    	SpringApplication.run(TraditionalServerApplication.class, args);
    }

}