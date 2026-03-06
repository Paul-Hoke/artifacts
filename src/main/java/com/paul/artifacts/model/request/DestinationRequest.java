package com.paul.artifacts.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DestinationRequest {

  @JsonProperty("x")
  private Integer x;

  @JsonProperty("y")
  private Integer y;

  @JsonProperty("map_id")
  private Integer mapId;
}
