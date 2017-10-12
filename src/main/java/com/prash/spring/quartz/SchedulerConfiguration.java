package com.prash.spring.quartz;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.ParseException;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Prashanth_Meka
 *
 */

@Configuration
public class SchedulerConfiguration {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	private ApplicationContext applicationContext;


	private final String INVOICE_CRON_EXP = "0 0 13 1/1 * ?";
	private final String INVOICE_ATTACHEMENT_CRON_EXP = "0 0 13 1/1 * ?";
	private final String REGISTRATION_EMAIL_CRON_EXP = "0/10 * * * * ?";
	private final String NOTIFICATION_CRON_EXP = "0 0 0/6 1/1 * ?";
	private final String LOGISTIC_SHIPPING_RECORD_UPDATE_CRON_EXP = "0 0 13 1/1 * ?";
	private final String LOGISTIC_AUTO_ARCHIVE_CRON_EXP = "0 0/15 * * * ?";
	private final String USER_ACCOUNT_STATUS_UPADTE_CORN_EXP="0 0 0 1/1 * ? *";
	
	
	@Bean
	public JobDetailFactoryBean invoiceJob() throws ClassNotFoundException, NoSuchMethodException {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setBeanName("InvoiceJob");
		jobDetailFactory.setJobClass(InvoiceJob.class);
		final Map<String, Object> map = new HashMap<>();
		map.put("allianceParam", "FDP");
		jobDetailFactory.setJobDataAsMap(map);
		jobDetailFactory.setGroup("MPGROUP");
		jobDetailFactory.setName("INVOICEJOB");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

	@Bean
	public JobDetailFactoryBean messageNotificationJob() throws ClassNotFoundException, NoSuchMethodException {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setBeanName("MessageNotificationJob");
		jobDetailFactory.setJobClass(MessageNotificationJob.class);
		jobDetailFactory.setGroup("MPGROUP");
		jobDetailFactory.setName("MESSAGENOTIFICATIONJOB");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

	@Bean
	public JobDetailFactoryBean registrationEmailJob() throws ClassNotFoundException, NoSuchMethodException {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setBeanName("REGISTRATIONEMAILJOB");
		jobDetailFactory.setJobClass(RegistrationEmailJob.class);
		jobDetailFactory.setGroup("MPGROUP");
		jobDetailFactory.setName("REGISTRATIONEMAILJOB");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

	@Bean
	public JobDetailFactoryBean invoiceWithAttachementJob() throws ClassNotFoundException, NoSuchMethodException {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setBeanName("INVOICEATTACHEMENTJOB");
		jobDetailFactory.setJobClass(InvoiceAttachementJob.class);
		final Map<String, Object> map = new HashMap<>();
		map.put("allianceParam", "FDP");
		jobDetailFactory.setJobDataAsMap(map);
		jobDetailFactory.setGroup("MPGROUP");
		jobDetailFactory.setName("INVOICEATTACHEMENTJOB");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

	@Bean
	public CronTriggerFactoryBean invoiceJobTrigger()
			throws ParseException, ClassNotFoundException, NoSuchMethodException {
		final CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setBeanName("MPJobTrigger");
		cronTriggerFactoryBean.setJobDetail(this.invoiceJob().getObject());
		cronTriggerFactoryBean.setCronExpression(this.INVOICE_CRON_EXP);
		cronTriggerFactoryBean.setName("INVOICETRIGGER");
		cronTriggerFactoryBean.setGroup("MPGROUP");
		return cronTriggerFactoryBean;
	}

	@Bean
	public CronTriggerFactoryBean messageNotificationJobTrigger()
			throws ParseException, ClassNotFoundException, NoSuchMethodException {
		final CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setBeanName("MPJobTrigger");
		cronTriggerFactoryBean.setJobDetail(this.messageNotificationJob().getObject());
		cronTriggerFactoryBean.setCronExpression(this.NOTIFICATION_CRON_EXP);
		cronTriggerFactoryBean.setName("NOTIFICATIONMAILTRIGGER");
		cronTriggerFactoryBean.setGroup("MPGROUP");
		return cronTriggerFactoryBean;
	}

	@Bean
	public CronTriggerFactoryBean registrationJobTrigger()
			throws ParseException, ClassNotFoundException, NoSuchMethodException {
		final CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setBeanName("MPJobTrigger");
		cronTriggerFactoryBean.setJobDetail(this.registrationEmailJob().getObject());
		cronTriggerFactoryBean.setCronExpression(this.REGISTRATION_EMAIL_CRON_EXP);
		cronTriggerFactoryBean.setName("REGISTRATIONEMAILTRIGGER");
		cronTriggerFactoryBean.setGroup("MPGROUP");
		return cronTriggerFactoryBean;
	}

	@Bean
	public CronTriggerFactoryBean invoiceAttachementJobTrigger()
			throws ParseException, ClassNotFoundException, NoSuchMethodException {
		final CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setBeanName("MPJobTrigger");
		cronTriggerFactoryBean.setJobDetail(this.invoiceWithAttachementJob().getObject());
		cronTriggerFactoryBean.setCronExpression(this.INVOICE_ATTACHEMENT_CRON_EXP);
		cronTriggerFactoryBean.setName("INVOICEATTACHEMENTTRIGGER");
		cronTriggerFactoryBean.setGroup("MPGROUP");
		return cronTriggerFactoryBean;
	}

	@Bean
	public CronTriggerFactoryBean logisticAutoArchiveJobTrigger()
			throws ParseException, ClassNotFoundException, NoSuchMethodException {
		final CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setBeanName("MPJobTrigger");
		cronTriggerFactoryBean.setJobDetail(this.logisticAutoArchiveJob().getObject());
		cronTriggerFactoryBean.setCronExpression(this.LOGISTIC_AUTO_ARCHIVE_CRON_EXP);
		cronTriggerFactoryBean.setName("LOGISTICAUTOARCHIVETRIGGER");
		cronTriggerFactoryBean.setGroup("MPGROUP");
		return cronTriggerFactoryBean;
	}

	@Bean
	public JobDetailFactoryBean logisticAutoArchiveJob() throws ClassNotFoundException, NoSuchMethodException {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setBeanName("LogisticAutoArchiveJob");
		jobDetailFactory.setJobClass(LogisticAutoArchiveJob.class);
		jobDetailFactory.setGroup("MPGROUP");
		jobDetailFactory.setName("LOGISTICAUTOARCHIVEJOB");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

	@Bean
	public CronTriggerFactoryBean logisticShipingRequestJobTrigger()
			throws ParseException, ClassNotFoundException, NoSuchMethodException {
		final CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setBeanName("MPJobTrigger");
		cronTriggerFactoryBean.setJobDetail(this.logisticShipingRequestJob().getObject());
		cronTriggerFactoryBean.setCronExpression(this.LOGISTIC_SHIPPING_RECORD_UPDATE_CRON_EXP);
		cronTriggerFactoryBean.setName("LOGISTICSHIPPINGREQUESTJOBTRIGGER");
		cronTriggerFactoryBean.setGroup("MPGROUP");
		return cronTriggerFactoryBean;
	}

	@Bean
	public JobDetailFactoryBean logisticShipingRequestJob() throws ClassNotFoundException, NoSuchMethodException {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setBeanName("LogisticShippingRequestJob");
		jobDetailFactory.setJobClass(LogisticShippingRequestJob.class);
		final Map<String, Object> map = new HashMap<>();
		map.put("allianceParam", "EMS");
		jobDetailFactory.setJobDataAsMap(map);
		jobDetailFactory.setGroup("MPGROUP");
		jobDetailFactory.setName("LOGISTICSHIPPINGREQUESTJOB");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

	@Bean
	public JobDetailFactoryBean userAccountStatusUpdateJob() throws ClassNotFoundException, NoSuchMethodException {
		final JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setBeanName("UserAccountStatusUpdateJob");
		jobDetailFactory.setJobClass(UserAccountStatusUpdateJob.class);
		jobDetailFactory.setGroup("MPGROUP");
		jobDetailFactory.setName("USERACCOUNTSTATUSUPDATEJOB");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}
	
	@Bean
	public CronTriggerFactoryBean userAccountStatusUpdateJobTrigger()
			throws ParseException, ClassNotFoundException, NoSuchMethodException {
		final CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setBeanName("MPJobTrigger");
		cronTriggerFactoryBean.setJobDetail(this.userAccountStatusUpdateJob().getObject());
		cronTriggerFactoryBean.setCronExpression(this.USER_ACCOUNT_STATUS_UPADTE_CORN_EXP);
		cronTriggerFactoryBean.setName("USERACCOUNTSTATUSUPDATEJOBTRIGGER");
		cronTriggerFactoryBean.setGroup("MPGROUP");
		return cronTriggerFactoryBean;
	}

	
	@Bean
	public SchedulerFactoryBean setupSchedulerFactoryBean()
			throws ClassNotFoundException, NoSuchMethodException, IOException {
		final SchedulerFactoryBean schedulerfaFactoryBean = new SchedulerFactoryBean();
		this.log.info("--------Scheduler Registration Started.---------");
		schedulerfaFactoryBean.setDataSource(this.dataSource);
		schedulerfaFactoryBean.setTransactionManager(this.transactionManager);
		schedulerfaFactoryBean.setTriggers(
					this.invoiceJobTrigger().getObject(),
					this.registrationJobTrigger().getObject(),
					this.invoiceAttachementJobTrigger().getObject(),
					this.messageNotificationJobTrigger().getObject(),
					this.logisticShipingRequestJobTrigger().getObject(),
					this.logisticAutoArchiveJobTrigger().getObject(),
					this.userAccountStatusUpdateJobTrigger().getObject()
					);
		schedulerfaFactoryBean.setQuartzProperties(this.quartzProperties());
		schedulerfaFactoryBean.setOverwriteExistingJobs(true);
		schedulerfaFactoryBean.setSchedulerName("MerchantPortal-scheduler-prashanth");
		schedulerfaFactoryBean.setWaitForJobsToCompleteOnShutdown(true);

		final AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(this.applicationContext);
		schedulerfaFactoryBean.setJobFactory(jobFactory);
		this.log.info("--------Scheduler Registered.---------");
		return schedulerfaFactoryBean;
	}

	@Bean
	public Properties quartzProperties() {
		final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		Properties properties = null;
		try {
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();

		} catch (final IOException e) {
			this.log.warn("Cannot load quartz.properties.");
		}

		return properties;
	}

	@PostConstruct
	public void init() {
		this.log.debug("QuartzConfig initialized.");
	}

}