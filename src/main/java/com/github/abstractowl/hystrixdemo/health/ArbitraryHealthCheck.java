package com.github.abstractowl.hystrixdemo.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.github.abstractowl.hystrixdemo.service.ThrottlingService;

@Component
public class ArbitraryHealthCheck implements HealthIndicator {
	@Autowired
	private ThrottlingService throttlingService;
	
	@Override
	public Health health() {
		try {
			throttlingService.getResult();
			return Health.up().build();
		} catch (Exception e) {
			return Health.down(e).build();
		}
	}
}
