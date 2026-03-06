package com.paul.artifacts.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenRequest {

  @JsonProperty("username")
  private String username;

  @JsonProperty("password")
  private String password;
}
