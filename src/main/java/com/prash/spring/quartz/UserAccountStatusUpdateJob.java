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
public class UserAccountStatusUpdateJob implements Job {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("-------- Started User Account Status Update Job for expiring accounts---");
		
		log.info("-------- End User Account Status Update trigger job ---");
	}
}
