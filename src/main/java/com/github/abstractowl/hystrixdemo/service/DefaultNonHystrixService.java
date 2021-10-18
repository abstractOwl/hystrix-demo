package com.github.abstractowl.hystrixdemo.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class DefaultNonHystrixService implements NonHystrixService {
	@Autowired
	private Random random;
	
	@Autowired
	MeterRegistry registry;

	@Timed("NonHystrixService:GetResult:Time")
	@Counted("NonHystrixService:GetResult:Count")
	@Override
	public String getResult() {
		if (random.nextBoolean()) {
			return "success";
		}
		throw new RuntimeException("failed");
	}
}