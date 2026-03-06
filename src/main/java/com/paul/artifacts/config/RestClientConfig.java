package com.paul.artifacts.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

  @Value("${artifacts.api-key}")
  private String apiKey;

  @Bean
  public RestClient artifactsRestClient() {
    log.info("Configuring Artifacts REST client → https://api.artifactsmmo.com");
    return RestClient.builder()
        .baseUrl("https://api.artifactsmmo.com")
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .defaultHeader("Accept", "application/json")
        .build();
  }
}
