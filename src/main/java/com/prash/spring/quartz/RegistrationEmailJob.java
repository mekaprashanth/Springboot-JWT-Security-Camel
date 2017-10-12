package com.prash.spring.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prash.spring.security.jwt.JwtTokenUtil;

/**
 * @author Prashanth_Meka
 *
 */

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution

public class RegistrationEmailJob implements Job {
	
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	private String someParam;
	private String someParam2;

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public void setSomeParam(String someParam) {
		this.someParam = someParam;
	}

	public void setSomeParam2(String someParam2) {
		this.someParam2 = someParam2;
	}

//	public RegistrationEmailJob() {
//		System.out.println(
//				"Job " + this.getClass().getSimpleName() + " is initialized by " + Thread.currentThread().getName());
//	}

	 @Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("-------- Started Email trigger job for inactive status ---");
		log.debug("-------- Started Email trigger job for inactive status ---");
		try	{
			jwtTokenUtil.validateToken("prashanth");
		}catch(Exception e)	{
			log.error("error", e);
		}

		log.info("-------- End Email trigger job for inactive status ---");
		log.debug("-------- End Email trigger job for inactive status ---");
	}

//	@Override
//	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		log.info("-------- Started Email trigger job for inactive status ---");
//		log.debug("-------- Started Email trigger job for inactive status ---");
//		System.out.println("-------- Started Email trigger job for inactive status ---");
//		
//		jwtTokenUtil.validateToken("prashanth");
//
//		log.info("-------- End Email trigger job for inactive status ---");
//		log.debug("-------- End Email trigger job for inactive status ---");
//
//	}
}