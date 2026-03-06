package com.paul.artifacts.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paul.artifacts.model.common.SimpleItem;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiveItemRequest {

  @JsonProperty("character_name")
  private String characterName;

  @JsonProperty("items")
  private List<SimpleItem> items;
}
