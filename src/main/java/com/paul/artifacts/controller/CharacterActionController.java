package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.request.*;
import com.paul.artifacts.model.ws.CharacterPositionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/my/{name}/action")
@RequiredArgsConstructor
public class CharacterActionController {

  private final ArtifactsApiClient client;
  private final SimpMessagingTemplate messagingTemplate;

  @PostMapping("/move")
  public JsonNode move(@PathVariable String name, @RequestBody DestinationRequest request) {
    JsonNode response = client.actionMove(name, request);
    JsonNode data = response.path("data");
    JsonNode character = data.path("character");
    if (!character.isMissingNode()) {
      double cooldownSeconds = data.path("cooldown").path("total_seconds").asDouble(0);
      CharacterPositionMessage msg = CharacterPositionMessage.builder()
          .name(character.path("name").asText())
          .skin(character.path("skin").asText())
          .x(character.path("x").asInt())
          .y(character.path("y").asInt())
          .cooldownSeconds(cooldownSeconds)
          .build();
      log.debug("Broadcasting position update for {}: ({}, {}) cooldown={}s", msg.getName(), msg.getX(), msg.getY(), cooldownSeconds);
      messagingTemplate.convertAndSend("/topic/characters", msg);
    }
    return response;
  }

  @PostMapping("/transition")
  public JsonNode transition(@PathVariable String name) {
    return client.actionTransition(name);
  }

  @PostMapping("/rest")
  public JsonNode rest(@PathVariable String name) {
    return client.actionRest(name);
  }

  @PostMapping("/equip")
  public JsonNode equip(@PathVariable String name, @RequestBody EquipRequest request) {
    return client.actionEquip(name, request);
  }

  @PostMapping("/unequip")
  public JsonNode unequip(@PathVariable String name, @RequestBody UnequipRequest request) {
    return client.actionUnequip(name, request);
  }

  @PostMapping("/use")
  public JsonNode useItem(@PathVariable String name, @RequestBody UseItemRequest request) {
    return client.actionUseItem(name, request);
  }

  @PostMapping("/fight")
  public JsonNode fight(@PathVariable String name, @RequestBody(required = false) FightRequest request) {
    return client.actionFight(name, request);
  }

  @PostMapping("/gathering")
  public JsonNode gathering(@PathVariable String name) {
    return client.actionGathering(name);
  }

  @PostMapping("/crafting")
  public JsonNode crafting(@PathVariable String name, @RequestBody CraftRequest request) {
    return client.actionCrafting(name, request);
  }

  @PostMapping("/bank/deposit/gold")
  public JsonNode depositBankGold(@PathVariable String name, @RequestBody GoldRequest request) {
    return client.actionDepositBankGold(name, request);
  }

  @PostMapping("/bank/deposit/item")
  public JsonNode depositBankItem(@PathVariable String name, @RequestBody ItemsRequest request) {
    return client.actionDepositBankItem(name, request);
  }

  @PostMapping("/bank/withdraw/item")
  public JsonNode withdrawBankItem(@PathVariable String name, @RequestBody ItemsRequest request) {
    return client.actionWithdrawBankItem(name, request);
  }

  @PostMapping("/bank/withdraw/gold")
  public JsonNode withdrawBankGold(@PathVariable String name, @RequestBody GoldRequest request) {
    return client.actionWithdrawBankGold(name, request);
  }

  @PostMapping("/bank/buy_expansion")
  public JsonNode buyBankExpansion(@PathVariable String name) {
    return client.actionBuyBankExpansion(name);
  }

  @PostMapping("/npc/buy")
  public JsonNode npcBuy(@PathVariable String name, @RequestBody NpcBuyRequest request) {
    return client.actionNpcBuy(name, request);
  }

  @PostMapping("/npc/sell")
  public JsonNode npcSell(@PathVariable String name, @RequestBody NpcSellRequest request) {
    return client.actionNpcSell(name, request);
  }

  @PostMapping("/recycling")
  public JsonNode recycling(@PathVariable String name, @RequestBody RecyclingRequest request) {
    return client.actionRecycling(name, request);
  }

  @PostMapping("/grandexchange/buy")
  public JsonNode geBuy(@PathVariable String name, @RequestBody GeBuyRequest request) {
    return client.actionGeBuy(name, request);
  }

  @PostMapping("/grandexchange/create-sell-order")
  public JsonNode geCreateSellOrder(@PathVariable String name, @RequestBody GeSellOrderRequest request) {
    return client.actionGeCreateSellOrder(name, request);
  }

  @PostMapping("/grandexchange/cancel")
  public JsonNode geCancel(@PathVariable String name, @RequestBody GeCancelRequest request) {
    return client.actionGeCancel(name, request);
  }

  @PostMapping("/grandexchange/create-buy-order")
  public JsonNode geCreateBuyOrder(@PathVariable String name, @RequestBody GeBuyOrderRequest request) {
    return client.actionGeCreateBuyOrder(name, request);
  }

  @PostMapping("/grandexchange/fill")
  public JsonNode geFill(@PathVariable String name, @RequestBody GeFillRequest request) {
    return client.actionGeFill(name, request);
  }

  @PostMapping("/task/complete")
  public JsonNode completeTask(@PathVariable String name) {
    return client.actionCompleteTask(name);
  }

  @PostMapping("/task/exchange")
  public JsonNode taskExchange(@PathVariable String name) {
    return client.actionTaskExchange(name);
  }

  @PostMapping("/task/new")
  public JsonNode acceptNewTask(@PathVariable String name) {
    return client.actionAcceptNewTask(name);
  }

  @PostMapping("/task/trade")
  public JsonNode taskTrade(@PathVariable String name, @RequestBody TaskTradeRequest request) {
    return client.actionTaskTrade(name, request);
  }

  @PostMapping("/task/cancel")
  public JsonNode taskCancel(@PathVariable String name) {
    return client.actionTaskCancel(name);
  }

  @PostMapping("/give/gold")
  public JsonNode giveGold(@PathVariable String name, @RequestBody GiveGoldRequest request) {
    return client.actionGiveGold(name, request);
  }

  @PostMapping("/give/item")
  public JsonNode giveItems(@PathVariable String name, @RequestBody GiveItemRequest request) {
    return client.actionGiveItems(name, request);
  }

  @PostMapping("/claim_item/{id}")
  public JsonNode claimPendingItem(@PathVariable String name, @PathVariable String id) {
    return client.actionClaimPendingItem(name, id);
  }

  @PostMapping("/delete")
  public JsonNode deleteItem(@PathVariable String name, @RequestBody DeleteItemRequest request) {
    return client.actionDeleteItem(name, request);
  }

  @PostMapping("/change_skin")
  public JsonNode changeSkin(@PathVariable String name, @RequestBody ChangeSkinRequest request) {
    return client.actionChangeSkin(name, request);
  }
}
