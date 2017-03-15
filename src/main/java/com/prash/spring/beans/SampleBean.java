package com.prash.spring.beans;

import org.springframework.stereotype.Component;

@Component("myBean")
public class SampleBean {
	public String saySomething()	{
		return "Hello World";
	}
}
