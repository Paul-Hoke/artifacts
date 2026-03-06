package com.paul.artifacts.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NpcBuyRequest {

  @JsonProperty("code")
  private String code;

  @JsonProperty("quantity")
  private Integer quantity;
}
