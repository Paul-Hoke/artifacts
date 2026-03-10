package com.paul.artifacts.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

  @Value("${artifacts.api-key}")
  private String apiKey;

  @Bean
  public RestClient artifactsRestClient() {
    log.info("Configuring Artifacts REST client → https://api.artifactsmmo.com");

    ClientHttpRequestInterceptor loggingInterceptor = (request, body, execution) -> {
      if (body.length > 0) {
        log.info("→ {} {} body={}", request.getMethod(), request.getURI(), new String(body));
      } else {
        log.info("→ {} {}", request.getMethod(), request.getURI());
      }
      var response = execution.execute(request, body);
      log.info("← {} {} {}", response.getStatusCode(), request.getMethod(), request.getURI());
      return response;
    };

    return RestClient.builder()
        .baseUrl("https://api.artifactsmmo.com")
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .defaultHeader("Accept", "application/json")
        .defaultHeader("Content-Type", "application/json")
        .requestInterceptor(loggingInterceptor)
        .build();
  }
}
