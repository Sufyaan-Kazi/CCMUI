package com.pivotal.fieldengineering.sampledata.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

//@Configuration
//@EnableAutoConfiguration
//@EnableConfigurationProperties
//@ConfigurationProperties(prefix = "spring.redis")
//@Profile("local")
public class StaticData {
	
	@Autowired
	public Environment env;

	public StaticData() {
		super();
	}
	
	public static final StaticData INSTANCE = new StaticData();
	
	@Value("${CF_DOMAIN:123}")
	public String CF_DOMAIN;

	public String getCF_DOMAIN() {
		return CF_DOMAIN;
	}

	public void setCF_DOMAIN(String cF_DOMAIN) {
		CF_DOMAIN = cF_DOMAIN;
	}
}
