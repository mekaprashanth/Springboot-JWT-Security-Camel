//package com.prash.spring.config.embedded.support;
//
//import org.apache.camel.CamelContext;
//import org.apache.camel.Exchange;
//import org.apache.camel.ExchangePattern;
//import org.apache.camel.Processor;
//import org.apache.camel.ProducerTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.tempuri.Divide;
//
//@SpringBootApplication
//@SuppressWarnings("all")
//public class MainApplication implements CommandLineRunner {
//
//	Logger log = LoggerFactory.getLogger(this.getClass());
//
//	@Autowired
//	CamelContext camelContext;
//
//	public static void main(String[] args) {
//		SpringApplication.run(MainApplication.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		 requestTest();
////		requestBodyTest();
////		requestRestAPI();
//	}
//
//	private void requestRestAPI() {
//		final ProducerTemplate template = this.camelContext.createProducerTemplate();
//		try {
//			final Object camelResponse = template.request("direct:restapi",(e)->{});
//			// log.debug("camelResponse.getException() {} ", camelResponse.getException());
//			// log.debug("camelResponse.getProperty(Exchange.EXCEPTION_CAUGHT) {} ",
//			// camelResponse.getProperty(Exchange.EXCEPTION_CAUGHT));
//			log.debug("Response received {}", camelResponse);
//		} catch (Exception e) {
//			log.error("error in MainApplication************", e);
//		}
//	}
//
//	private void requestBodyTest() {
//		final ProducerTemplate template = this.camelContext.createProducerTemplate();
//		try {
//			Divide divide = new Divide();
//			divide.setIntA(20);
//			divide.setIntB(2);
//			final Object camelResponse = template.requestBody("direct:calculatorEP", divide);
//			// log.debug("camelResponse.getException() {} ", camelResponse.getException());
//			// log.debug("camelResponse.getProperty(Exchange.EXCEPTION_CAUGHT) {} ",
//			// camelResponse.getProperty(Exchange.EXCEPTION_CAUGHT));
//			log.debug("Response received {}", camelResponse);
//		} catch (Exception e) {
//			log.error("error in MainApplication************", e);
//		}
//	}
//
//	/**
//	 * 
//	 */
//	private void requestTest() {
//		final ProducerTemplate template = this.camelContext.createProducerTemplate();
//		final Exchange camelResponse = template.request("direct:calculatorEP", new Processor() {
//			@Override
//			public void process(final Exchange exchange) throws Exception {
//				exchange.setPattern(ExchangePattern.InOut);
//				Divide divide = new Divide();
//				divide.setIntA(20);
//				divide.setIntB(2);
//				exchange.getIn().setBody(divide);
//			}
//		});
//		 log.debug("camelResponse.getException() {}", camelResponse.getException());
//		 log.debug("camelResponse.getProperty(Exchange.EXCEPTION_CAUGHT) {}",
//		 camelResponse.getProperty(Exchange.EXCEPTION_CAUGHT));
//		log.debug("Response received {}", camelResponse.getOut());
//	}
//
//}
