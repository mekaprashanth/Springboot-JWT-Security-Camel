package com.prash.spring.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MessageNotificationJob implements Job {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		log.info("--------------- NOTIFICATION JOB STARTED ---------------");
		
		log.info("---------------  NOTIFICATION JOB  END ---------------");

	}

}
