package com.paul.artifacts.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnequipRequest {

  @JsonProperty("slot")
  private String slot;

  @JsonProperty("quantity")
  private Integer quantity;
}
