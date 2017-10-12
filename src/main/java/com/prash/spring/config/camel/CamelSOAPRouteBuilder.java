/**
 * 
 */
package com.prash.spring.config.camel;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.CxfEndpointConfigurer;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tempuri.CalculatorSoap;

/**
 * @author f2u85i8
 *
 */

@Component
public class CamelSOAPRouteBuilder extends RouteBuilder {

	@Value("${calculatorwsdl.local.endpoint}")
	private String calculatorLocalAddress;

	@Value("${calculatorwsdl.remote.endpoint}")
	private String calculatorRemoteAddress;
	
	@Value("${dp.failover.enabled}")
	private boolean dpFailoverEnabled;
	
	

	@Override
	public void configure() throws Exception {
		final CamelContext camelContext = this.getContext();
		final CxfEndpoint primaryEndpoint = createEndpoint(camelContext, calculatorLocalAddress);
		final CxfEndpoint failoverEndpoint = createEndpoint(camelContext, calculatorRemoteAddress);

		failoverEndpoint.setCxfEndpointConfigurer(new CustomClientConfigurer());

		camelContext.addEndpoint("calculatorPrimaryEP", primaryEndpoint);
		camelContext.addEndpoint("calculatorFailoverEP", failoverEndpoint);

		onException(Exception.class).log("Unknown Exception occured").handled(true).end();
		
		
		from("direct:calculatorEP")
		.to("direct:calculator")
		.end();
		
		
		if(dpFailoverEnabled)	{
			setRouteWFailover();
		}else	{
			setRouteWOFailover();
		}
	}

	/**
	 * 
	 */
	private void setRouteWOFailover() {
		from("direct:calculator")
		.to("calculatorPrimaryEP")
		.end();
	}

	/**
	 * 
	 */
	private void setRouteWFailover() {
		from("direct:calculator")
		.loadBalance().failover(1,false,false, IOException.class)
		.to("calculatorPrimaryEP")
		.to("calculatorFailoverEP")
		.end();
	}

	private CxfEndpoint createEndpoint(final CamelContext camelContext, final String address) {

		final CxfEndpoint cxfEndpoint = new CxfEndpoint();
		cxfEndpoint.setCamelContext(camelContext);
		cxfEndpoint.setAddress(address);
		cxfEndpoint.setDataFormat(DataFormat.POJO);
		cxfEndpoint.setServiceClass(CalculatorSoap.class);

		cxfEndpoint.getInInterceptors().add(new LoggingInInterceptor());
		cxfEndpoint.getOutInterceptors().add(new LoggingOutInterceptor());
		return cxfEndpoint;
	}

	private static class CustomClientConfigurer implements CxfEndpointConfigurer {

		@Override
		public void configure(final AbstractWSDLBasedEndpointFactory arg0) {
			//do nothing
		}

		@Override
		public void configureClient(final Client client) {
			final HTTPConduit httpConduit = (HTTPConduit) client.getConduit();

			httpConduit.getClient().setProxyServer("10.84.131.10");
			httpConduit.getClient().setProxyServerPort(8080);
			httpConduit.getProxyAuthorization().setUserName("f2u85i8");
			httpConduit.getProxyAuthorization().setPassword("sep#12345");

		}

		@Override
		public void configureServer(final Server arg0) {
				//do nothing
		}

	}

}
