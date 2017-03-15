package com.prash.spring.config.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SampleCamelRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:hello?period={{timer.period}}")
                .transform(method("myBean", "saySomething"))
                .log("${body}");
    }

}