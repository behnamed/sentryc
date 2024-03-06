package com.sentryc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSentrycApplication {

	public static void main(String[] args) {
		SpringApplication.from(SentrycApplication::main).with(TestSentrycApplication.class).run(args);
	}

}
