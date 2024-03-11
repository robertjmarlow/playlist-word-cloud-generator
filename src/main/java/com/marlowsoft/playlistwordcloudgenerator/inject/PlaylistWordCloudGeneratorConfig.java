package com.marlowsoft.playlistwordcloudgenerator.inject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.http.HttpClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PlaylistWordCloudGeneratorConfig {
  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public ObjectMapper getObjectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public HttpClient.Builder getHttpClientBuilder() {
    return HttpClient.newBuilder();
  }
}
