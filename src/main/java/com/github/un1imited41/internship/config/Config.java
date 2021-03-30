package com.github.un1imited41.internship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class Config {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
