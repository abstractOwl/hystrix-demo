package com.github.abstractowl.hystrixdemo.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

@Service
public class SimpleFaultyService implements FaultyService {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleFaultyService.class);

	private Random random;
	private double successProbability;

	@Autowired
	public SimpleFaultyService(Random random, @Qualifier("successProbability") double successProbability) {
		this.random = new Random();
		this.successProbability = successProbability;
	}

	@Override
	@Counted("SimpleFaultyService.Count")
	@Timed("SimpleFaultyService.Time")
	@HystrixCommand(fallbackMethod = "fallback")
	public String getResult() {
		boolean success = random.nextFloat() < successProbability;
		
		LOG.debug("result: {}", success);
		if (success) {
			return "success";
		} else {
			throw new RuntimeException("service failed");
		}
	}
	
	@SuppressWarnings("unused")
	private String fallback() {
		return "fallback";
	}
}