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
public class InvoiceAttachementJob implements Job {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


	private String allianceParam;

	public void setAllianceParam(String allianceParam) {
		this.allianceParam = allianceParam;
	}

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		log.info("--------------- Invoice Attachement BATCH JOB STARTED ---------------");
		
		log.info("--------------- Invoice Attachement BATCH JOB END ---------------");
	}
}
