package com.pivotal.fieldengineering.sampledata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.pivotal.fieldengineering.sampledata.utils.Logger;

@Configuration
@ConfigurationProperties(prefix="spring.redis")
@Profile("local")
public class RedisConfig {

	private String redisHostName;
	private int redisPort;

	public RedisConfig() {
		super();
	}
	
	public String getRedisHostName() {
		return redisHostName;
	}

	public void setRedisHostName(String redisHostName) {
		this.redisHostName = redisHostName;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		//Logger.INSTANCE.log("Creating Jedis Factory: " + this.redisHostName + ":" + this.redisPort);

		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHostName);
		factory.setPort(redisPort);
		Logger.INSTANCE.log("Conected to Redis: " + factory.getHostName() + ":" + factory.getPort());
		return factory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}
}
