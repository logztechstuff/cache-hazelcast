package com.logztechstuff.cache.hazelcast.repo;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.hazelcast.core.IMap;
import com.hazelcast.spring.cache.HazelcastCacheManager;

@Repository
public class HelloRepo {
	
	private static final Logger LOG = LoggerFactory.getLogger(HelloRepo.class); 
	
	@Autowired
	private CacheManager cacheManager;
	
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	
	@Cacheable(cacheNames = "hello")
	public String getMessage(String name){
		try {
			Thread.sleep(5000);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "Hello " + name;
	}
	
	@PostConstruct
	public void init() {
		LOG.info("Cache Manager Class::{}", cacheManager.getClass());
		HazelcastCacheManager hazelcastCacheManager = (HazelcastCacheManager) cacheManager;
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				LOG.info("Cache names::{}", cacheManager.getCacheNames());
				LOG.info("Cache Manager Name::{}", hazelcastCacheManager.getHazelcastInstance().getName());
				Cache cache = cacheManager.getCache("hello");
				IMap<Object, Object> map = (IMap<Object, Object>) cache.getNativeCache();
				LOG.info("Size::{}", map.size());
				
				/*IMap<Object, Object> imap = hazelcastCacheManager.getHazelcastInstance().getMap("crawler-app");
				LOG.info("IMap Size::" + imap.size());*/
				
				/*Map<String, CacheSimpleConfig> cacheConfigs = hazelcastCacheManager.getHazelcastInstance().getConfig().getCacheConfigs();
				for(Map.Entry<String, CacheSimpleConfig> config : cacheConfigs.entrySet()) {
					LOG.info("Cache::" + config.getKey());
				}*/
			}
		}, 10, 10, TimeUnit.SECONDS);
	}
}

