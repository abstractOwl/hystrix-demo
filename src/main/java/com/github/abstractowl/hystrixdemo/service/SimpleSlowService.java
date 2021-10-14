package com.github.abstractowl.hystrixdemo.service;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class SimpleSlowService implements SlowService {
	@HystrixCommand(
			fallbackMethod = "fallback",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
					@HystrixProperty(name = "execution.timeout.enabled", value = "true"),
					@HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "5")
			}
	)
	@Override
	public String getResult() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.interrupted();
			throw new RuntimeException("interrupted", e);
		}
		return "success";
	}
	
	@SuppressWarnings("unused")
	private String fallback() {
		return "fallback";
	}
}
