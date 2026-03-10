package com.paul.artifacts.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.model.common.SimpleItem;
import com.paul.artifacts.model.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtifactsApiClient {

  private final RestClient artifactsRestClient;

  // -------------------------------------------------------------------------
  // Server
  // -------------------------------------------------------------------------

  public JsonNode getServerDetails() {
    return artifactsRestClient.get()
        .uri("/")
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // My Account
  // -------------------------------------------------------------------------

  public JsonNode getBankDetails() {
    return artifactsRestClient.get()
        .uri("/my/bank")
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getBankItems(String itemCode, Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/my/bank/items")
            .queryParamIfPresent("item_code", Optional.ofNullable(itemCode))
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getMyGeOrders(String code, String type, Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/my/grandexchange/orders")
            .queryParamIfPresent("code", Optional.ofNullable(code))
            .queryParamIfPresent("type", Optional.ofNullable(type))
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getMyGeHistory(String id, String code, Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/my/grandexchange/history")
            .queryParamIfPresent("id", Optional.ofNullable(id))
            .queryParamIfPresent("code", Optional.ofNullable(code))
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAccountDetails() {
    return artifactsRestClient.get()
        .uri("/my/details")
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode changePassword(ChangePasswordRequest request) {
    return artifactsRestClient.post()
        .uri("/my/change_password")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getPendingItems(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/my/pending-items")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // My Characters — Actions
  // -------------------------------------------------------------------------

  public JsonNode actionMove(String name, DestinationRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/move", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionTransition(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/transition", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionRest(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/rest", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionEquip(String name, EquipRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/equip", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionUnequip(String name, UnequipRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/unequip", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionUseItem(String name, UseItemRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/use", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionFight(String name, FightRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/fight", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGathering(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/gathering", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionCrafting(String name, CraftRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/crafting", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionDepositBankGold(String name, GoldRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/bank/deposit/gold", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionDepositBankItem(String name, List<SimpleItem> items) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/bank/deposit/item", name)
        .body(items)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionWithdrawBankItem(String name, List<SimpleItem> items) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/bank/withdraw/item", name)
        .body(items)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionWithdrawBankGold(String name, GoldRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/bank/withdraw/gold", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionBuyBankExpansion(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/bank/buy_expansion", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionNpcBuy(String name, NpcBuyRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/npc/buy", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionNpcSell(String name, NpcSellRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/npc/sell", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionRecycling(String name, RecyclingRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/recycling", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGeBuy(String name, GeBuyRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/grandexchange/buy", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGeCreateSellOrder(String name, GeSellOrderRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/grandexchange/create-sell-order", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGeCancel(String name, GeCancelRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/grandexchange/cancel", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGeCreateBuyOrder(String name, GeBuyOrderRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/grandexchange/create-buy-order", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGeFill(String name, GeFillRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/grandexchange/fill", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionCompleteTask(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/task/complete", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionTaskExchange(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/task/exchange", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionAcceptNewTask(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/task/new", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionTaskTrade(String name, TaskTradeRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/task/trade", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionTaskCancel(String name) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/task/cancel", name)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGiveGold(String name, GiveGoldRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/give/gold", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionGiveItems(String name, GiveItemRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/give/item", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionClaimPendingItem(String name, String id) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/claim_item/{id}", name, id)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionDeleteItem(String name, DeleteItemRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/delete", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode actionChangeSkin(String name, ChangeSkinRequest request) {
    return artifactsRestClient.post()
        .uri("/my/{name}/action/change_skin", name)
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // My Characters — Logs & Listing
  // -------------------------------------------------------------------------

  public JsonNode getAllCharactersLogs(Integer page) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/my/logs")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getCharacterLogs(String name, Integer page) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/my/logs/{name}")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .build(name))
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getMyCharacters() {
    return artifactsRestClient.get()
        .uri("/my/characters")
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Accounts
  // -------------------------------------------------------------------------

  public JsonNode createAccount(CreateAccountRequest request) {
    return artifactsRestClient.post()
        .uri("/accounts/create")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode forgotPassword(ForgotPasswordRequest request) {
    return artifactsRestClient.post()
        .uri("/accounts/forgot_password")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode resetPassword(ResetPasswordRequest request) {
    return artifactsRestClient.post()
        .uri("/accounts/reset_password")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAccountAchievements(String account) {
    return artifactsRestClient.get()
        .uri("/accounts/{account}/achievements", account)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAccountCharacters(String account) {
    return artifactsRestClient.get()
        .uri("/accounts/{account}/characters", account)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAccount(String account) {
    return artifactsRestClient.get()
        .uri("/accounts/{account}", account)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Achievements
  // -------------------------------------------------------------------------

  public JsonNode getAllAchievements(String type, Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/achievements")
            .queryParamIfPresent("type", Optional.ofNullable(type))
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAchievement(String code) {
    return artifactsRestClient.get()
        .uri("/achievements/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Badges
  // -------------------------------------------------------------------------

  public JsonNode getAllBadges(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/badges")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getBadge(String code) {
    return artifactsRestClient.get()
        .uri("/badges/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Characters (public)
  // -------------------------------------------------------------------------

  public JsonNode createCharacter(CreateCharacterRequest request) {
    return artifactsRestClient.post()
        .uri("/characters/create")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode deleteCharacter(DeleteCharacterRequest request) {
    return artifactsRestClient.post()
        .uri("/characters/delete")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getActiveCharacters(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/characters/active")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getCharacter(String name) {
    return artifactsRestClient.get()
        .uri("/characters/{name}", name)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Effects
  // -------------------------------------------------------------------------

  public JsonNode getAllEffects(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/effects")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getEffect(String code) {
    return artifactsRestClient.get()
        .uri("/effects/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Events
  // -------------------------------------------------------------------------

  public JsonNode getAllEvents(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/events")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAllActiveEvents(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/events/active")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode spawnEvent(SpawnEventRequest request) {
    return artifactsRestClient.post()
        .uri("/events/spawn")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Grand Exchange (public)
  // -------------------------------------------------------------------------

  public JsonNode getGeOrders(String code, Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/grandexchange/orders")
            .queryParamIfPresent("code", Optional.ofNullable(code))
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getGeOrder(String id) {
    return artifactsRestClient.get()
        .uri("/grandexchange/orders/{id}", id)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getGeHistory(String code, Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/grandexchange/history/{code}")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build(code))
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Items
  // -------------------------------------------------------------------------

  public JsonNode getAllItems(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/items")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getItem(String code) {
    return artifactsRestClient.get()
        .uri("/items/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Leaderboard
  // -------------------------------------------------------------------------

  public JsonNode getAccountsLeaderboard(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/leaderboard/accounts")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getCharactersLeaderboard(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/leaderboard/characters")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Maps
  // -------------------------------------------------------------------------

  public JsonNode getAllMaps(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/maps")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getLayerMaps(String layer, Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/maps/{layer}")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build(layer))
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getMapByPosition(String layer, Integer x, Integer y) {
    return artifactsRestClient.get()
        .uri("/maps/{layer}/{x}/{y}", layer, x, y)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getMapById(Integer mapId) {
    return artifactsRestClient.get()
        .uri("/maps/id/{map_id}", mapId)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Monsters
  // -------------------------------------------------------------------------

  public JsonNode getAllMonsters(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/monsters")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getMonster(String code) {
    return artifactsRestClient.get()
        .uri("/monsters/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // NPCs
  // -------------------------------------------------------------------------

  public JsonNode getAllNpcs(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/npcs/details")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getNpc(String code) {
    return artifactsRestClient.get()
        .uri("/npcs/details/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAllNpcItems(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/npcs/items")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getNpcItems(String code) {
    return artifactsRestClient.get()
        .uri("/npcs/items/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Resources
  // -------------------------------------------------------------------------

  public JsonNode getAllResources(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/resources")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getResource(String code) {
    return artifactsRestClient.get()
        .uri("/resources/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Tasks
  // -------------------------------------------------------------------------

  public JsonNode getAllTasks(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/tasks/list")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getTask(String code) {
    return artifactsRestClient.get()
        .uri("/tasks/list/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getAllTaskRewards(Integer page, Integer size) {
    return artifactsRestClient.get()
        .uri(b -> b.path("/tasks/rewards")
            .queryParamIfPresent("page", Optional.ofNullable(page))
            .queryParamIfPresent("size", Optional.ofNullable(size))
            .build())
        .retrieve()
        .body(JsonNode.class);
  }

  public JsonNode getTaskReward(String code) {
    return artifactsRestClient.get()
        .uri("/tasks/rewards/{code}", code)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Simulation
  // -------------------------------------------------------------------------

  public JsonNode fightSimulation(Object request) {
    return artifactsRestClient.post()
        .uri("/simulation/fight")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }

  // -------------------------------------------------------------------------
  // Token
  // -------------------------------------------------------------------------

  public JsonNode generateToken(TokenRequest request) {
    return artifactsRestClient.post()
        .uri("/token")
        .body(request)
        .retrieve()
        .body(JsonNode.class);
  }
}
