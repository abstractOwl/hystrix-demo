package com.github.abstractowl.hystrixdemo.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

@Service
public class SimpleThrottlingService implements ThrottlingService {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleThrottlingService.class);
	
	private RateLimiter rateLimiter;
	
	public SimpleThrottlingService(RateLimiter rateLimiter) {
		this.rateLimiter = Objects.requireNonNull(rateLimiter);
	}
	
	@Autowired
	public SimpleThrottlingService(@Qualifier("allowedCallsPerSecond") double allowedCallsPerSecond) {
		this(RateLimiter.create(allowedCallsPerSecond));
	}

	@Override
	@Counted("SimpleThrottlingService.Count")
	@Timed("SimpleThrottlingService.Time")
	@HystrixCommand(
			fallbackMethod = "fallback",
			commandProperties = {
					// By default, if more than 50% of last >20 requests failed, open the circuit
					// (fail all calls) for 5 seconds.
					// https://github.com/Netflix/Hystrix/wiki/Configuration#circuit-breaker
					@HystrixProperty(name = "circuitBreaker.enabled", value = "true")
			}
	)
	public String getResult() {
		if (rateLimiter.tryAcquire()) {
			return "success";
		} else {
			LOG.error("throttled!"); // Log actual service calls vs circuit breaker
			throw new RuntimeException("throttled!");
		}
	}
	
	@SuppressWarnings("unused")
	private String fallback() {
		return "fallback";
	}
}
