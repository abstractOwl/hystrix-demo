package com.github.abstractowl.hystrixdemo.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.abstractowl.hystrixdemo.service.FaultyService;
import com.github.abstractowl.hystrixdemo.service.SlowService;
import com.github.abstractowl.hystrixdemo.service.ThrottlingService;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/")
public class DemoController {
	private FaultyService faultyService;
	private ThrottlingService throttlingService;
	private SlowService slowService;
	
	@Autowired
	public DemoController(FaultyService faultyService, SlowService slowService, ThrottlingService throttlingService) {
		this.faultyService = Objects.requireNonNull(faultyService);
		this.slowService = Objects.requireNonNull(slowService);
		this.throttlingService = Objects.requireNonNull(throttlingService);
	}
	
	@GetMapping("/faulty")
	@Counted
	@Timed
	public String faulty() {
		return faultyService.getResult();
	}
	
	@GetMapping("/throttling")
	@Counted
	@Timed
	public String throttling() {
		return throttlingService.getResult();
	}
	
	@GetMapping("/slow")
	@Counted
	@Timed
	public String slow() {
		return slowService.getResult();
	}
}
