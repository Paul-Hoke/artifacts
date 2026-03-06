package com.paul.artifacts.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCharacterRequest {

  @JsonProperty("name")
  private String name;

  @JsonProperty("skin")
  private Integer skin;
}
