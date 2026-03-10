package com.paul.artifacts.model.ws;

import com.paul.artifacts.model.common.SimpleItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BankStateMessage {

  private List<SimpleItem> items;
}
