package com.github.abstractowl.hystrixdemo.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class DefaultSlowService implements SlowService {
	@Autowired
	private Random random;

	@HystrixCommand(
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
					@HystrixProperty(name = "execution.timeout.enabled", value = "true"),
					@HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "5")
			}
	)
	@Override
	public String getResult() {
		try {
			Thread.sleep(800 + random.nextInt(400));
		} catch (InterruptedException e) {
			Thread.interrupted();
			throw new RuntimeException("interrupted", e);
		}
		return "success";
	}
}
