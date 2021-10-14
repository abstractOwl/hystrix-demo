package com.github.abstractowl.hystrixdemo.config;

import java.util.Random;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
@EnableAspectJAutoProxy
public class HystrixDemoConfiguration {
	@Bean
	public HystrixCommandAspect hystrixCommandAspect() {
		return new HystrixCommandAspect();
	}
	
	@Bean
	public CountedAspect countedAspect(MeterRegistry meterRegistry) {
		return new CountedAspect(meterRegistry);
	}
	
	@Bean
	public TimedAspect timedAspect(MeterRegistry meterRegistry) {
		return new TimedAspect(meterRegistry);
	}
	
	@Bean
	public Random randomGenerator() {
		return new Random();
	}
	
	@Bean
	@Qualifier("successProbability")
	public double successProbability() {
		return 0.5;
	}
	
	@Bean
	@Qualifier("allowedCallsPerSecond")
	public double allowedCallsPerSecond() {
		return 0.5;
	}
}
