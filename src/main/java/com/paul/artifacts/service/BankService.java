package com.paul.artifacts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.common.SimpleItem;
import com.paul.artifacts.model.ws.BankStateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

  private final ArtifactsApiClient client;
  private final SimpMessagingTemplate messagingTemplate;

  public void broadcastBankState() {
    try {
      List<SimpleItem> items = fetchAllBankItems();
      messagingTemplate.convertAndSend("/topic/bank",
          BankStateMessage.builder().items(items).build());
      log.debug("Broadcast bank state: {} item types", items.size());
    } catch (Exception e) {
      log.warn("Failed to broadcast bank state: {}", e.getMessage());
    }
  }

  /** Returns a map of item code → quantity for everything currently in the bank. */
  public Map<String, Integer> getBankInventoryMap() {
    return fetchAllBankItems().stream()
        .collect(Collectors.toMap(SimpleItem::getCode, SimpleItem::getQuantity));
  }

  private List<SimpleItem> fetchAllBankItems() {
    List<SimpleItem> all = new ArrayList<>();
    int page = 1;
    int pages;
    do {
      JsonNode response = client.getBankItems(null, page, 100);
      JsonNode data = response.path("data");
      pages = response.path("pages").asInt(1);
      if (data.isArray()) {
        for (JsonNode node : data) {
          String code = node.path("code").asText(null);
          if (code != null && !code.isBlank()) {
            SimpleItem item = new SimpleItem();
            item.setCode(code);
            item.setQuantity(node.path("quantity").asInt(0));
            all.add(item);
          }
        }
      }
      page++;
    } while (page <= pages);
    return all;
  }
}
