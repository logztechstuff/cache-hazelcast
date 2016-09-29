package com.logztechstuff.cache.hazelcast.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.logztechstuff.cache.hazelcast.repo.HelloRepo;

@RestController
public class HazelcastController {
	
	private static final Logger LOG = LoggerFactory.getLogger(HazelcastController.class);

	@Autowired
	private HelloRepo cacheRepo;
	
	@Autowired
	private CacheManager cacheManager;
	
	@GetMapping("/")
	public String sayHello() {
		LOG.info("Cache Manager::{}", cacheManager.getClass().getName());
		for(String name : cacheManager.getCacheNames()) {
			LOG.info("Cache Name::{}", name);
		}
		return "Hello dude!";
	}
	
	@GetMapping("/{name}")
	public String sayHello(@PathVariable(name = "name", required = true) String name) {
		long startTime = System.currentTimeMillis();
		String str = cacheRepo.getMessage(name);
		LOG.info("Msg::{} ;; Total time taken::{}ms", str, (System.currentTimeMillis() - startTime));
		return str;
	}
}
