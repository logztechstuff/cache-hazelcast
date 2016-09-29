package com.logztechstuff;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

@Configuration
public class HazelcastConfig {

	@Bean
	public Config getConfig(@Value("${hazelcast.cluster.ip}") List<String> ips) {
		NetworkConfig networkConfig = new NetworkConfig();
		TcpIpConfig tcpIpConfig = new TcpIpConfig();
		tcpIpConfig.setEnabled(true);
		tcpIpConfig.setMembers(ips);
		networkConfig.setJoin(new JoinConfig().setTcpIpConfig(tcpIpConfig).setMulticastConfig(new MulticastConfig().setEnabled(false)));
		return new Config("logztechstuff-cache")
				.setGroupConfig(new GroupConfig("local"))
				.setNetworkConfig(networkConfig)
				.addMapConfig(new MapConfig().setName("crawler-app")
							   .setEvictionPolicy(EvictionPolicy.LRU)
							   .setTimeToLiveSeconds(7200)
							   .setMaxIdleSeconds(3600));
	}
	
	@Bean
	public ConversionService conversionService() {
		return new DefaultConversionService();
	}
	
	@Bean
	@Profile("client")
	public HazelcastInstance hazelcastInstance() {
		return HazelcastClient.newHazelcastClient();
	}
	
	@Bean
	public CacheManager cacheManager() {
		return new HazelcastCacheManager(hazelcastInstance());
	}
}
