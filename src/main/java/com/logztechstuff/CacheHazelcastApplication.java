package com.logztechstuff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CacheHazelcastApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheHazelcastApplication.class, args);
		//new SpringApplicationBuilder().profiles("local").sources(CacheHazelcastApplication.class).run(args);
	}
}
