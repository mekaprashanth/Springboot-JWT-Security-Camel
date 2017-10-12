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
public class LogisticAutoArchiveJob implements Job {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	
	@Override
	public void execute(final JobExecutionContext ctx) throws JobExecutionException {
		this.log.info("--------------- Logistic JOB STARTED ---------------");
		
		this.log.info("---------------  Logistic JOB  END ---------------");

	}

}
