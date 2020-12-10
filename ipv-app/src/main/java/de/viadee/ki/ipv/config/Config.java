package de.viadee.ki.ipv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    public static final double SCHWELLWERT_ML_KONFIDENZ = 0.6;
    public static final double SCHWELLWERT_ANOMALIE = 0.3;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}