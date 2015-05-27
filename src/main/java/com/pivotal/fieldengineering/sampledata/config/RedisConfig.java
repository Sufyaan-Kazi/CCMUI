package com.pivotal.fieldengineering.sampledata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Profile("local")
public class RedisConfig {

	@Value("${host:Hello}") //Not sure why this isn't working?
	private String redisHostName;
	
	@Value("${port:123}")
	private int redisPort;

	public RedisConfig() {
		super();
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		System.out.println("Creating Jedis Factory: " + redisHostName + ":" + redisPort);

		JedisConnectionFactory factory = new JedisConnectionFactory();
		System.out.println("Conected to Redis: " + factory.getHostName() + ":" + factory.getPort());
		// factory.setHostName(redisHostName);
		// factory.setPort(redisPort);
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
}
