package com.paul.artifacts.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleItem {

  @JsonProperty("code")
  private String code;

  @JsonProperty("quantity")
  private Integer quantity;
}
