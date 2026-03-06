package com.paul.artifacts.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePasswordRequest {

  @JsonProperty("current_password")
  private String currentPassword;

  @JsonProperty("new_password")
  private String newPassword;
}
