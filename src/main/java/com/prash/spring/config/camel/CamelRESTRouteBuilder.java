/**
 * 
 */
package com.prash.spring.config.camel;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpClientConfigurer;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.component.http4.HttpEndpoint;
import org.apache.camel.component.http4.ProxyHttpClientConfigurer;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author f2u85i8
 *
 */

@Component
@java.lang.SuppressWarnings({"squid:CommentedOutCodeLine","squid:S1186"})
public class CamelRESTRouteBuilder extends RouteBuilder {

	@Value("${public.rest.local.endpoint}")
	private String publicRestLocalAddress;

	@Value("${public.rest.remote.endpoint}")
	private String publicRestRemoteAddress;

	@Value("${dp.failover.enabled}")
	private boolean dpFailoverEnabled;

	@Override
	public void configure() throws Exception {

		HttpComponent component = getContext().getComponent("https4", HttpComponent.class);
		HttpClientConfigurer configurer = new ProxyHttpClientConfigurer("10.84.131.10", 8080, "http", "f2u85i8",
				"sep#12345", null, null);
		component.setHttpClientConfigurer(configurer);
		
		component.setX509HostnameVerifier(NoopHostnameVerifier.INSTANCE);
		 
        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        X509ExtendedTrustManager extendedTrustManager = new InsecureX509TrustManager();
        trustManagersParameters.setTrustManager(extendedTrustManager);
 
        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setTrustManagers(trustManagersParameters);
        component.setSslContextParameters(sslContextParameters);

		HttpEndpoint endpoint = (HttpEndpoint)component.createEndpoint(publicRestRemoteAddress);

		from("direct:restapi")
		.to(endpoint)
		.log("${body}")
		.end();
	}

	private static class InsecureX509TrustManager extends X509ExtendedTrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
				throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
				throws CertificateException {

		}

		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
				throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
				throws CertificateException {

		}

		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

}
