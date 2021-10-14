package com.github.abstractowl.hystrixdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

@SpringBootApplication
public class HystrixDemoApplication {
	
	public static void main(String[] args) {
		Metrics.addRegistry(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
		SpringApplication.run(HystrixDemoApplication.class, args);
	}

}
