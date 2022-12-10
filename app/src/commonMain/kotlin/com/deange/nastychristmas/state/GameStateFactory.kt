package com.deange.nastychristmas.state

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.settings.GameSettings
import com.deange.nastychristmas.workflow.AppState
import com.deange.nastychristmas.workflow.AppState.ChangeGameSettings
import com.deange.nastychristmas.workflow.AppState.EndGame
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.deange.nastychristmas.workflow.AppState.OpeningGift
import com.deange.nastychristmas.workflow.AppState.PickingPlayer
import com.deange.nastychristmas.workflow.AppState.StealingRound

fun AppState.asGameState(): GameState = when (this) {
  is InitializingPlayers -> {
    GameState(
      allPlayers = allPlayers,
      playerPool = allPlayers.toSet(),
      roundNumber = -1,
      currentPlayer = null,
      gifts = GiftOwners(emptyMap()),
      stats = GameStats(),
      settings = GameSettings.Default,
    )
  }
  is PickingPlayer -> {
    GameState(
      allPlayers = allPlayers,
      playerPool = playerPool,
      roundNumber = round,
      currentPlayer = selectedPlayer,
      gifts = gifts,
      stats = stats,
      settings = settings,
    )
  }
  is StealingRound -> {
    GameState(
      allPlayers = allPlayers,
      playerPool = playerPool,
      roundNumber = round,
      currentPlayer = startingPlayer,
      gifts = gifts,
      stats = stats,
      settings = settings,
    )
  }
  is OpeningGift -> {
    GameState(
      allPlayers = allPlayers,
      playerPool = playerPool,
      roundNumber = round,
      currentPlayer = player,
      gifts = gifts,
      stats = stats,
      settings = settings,
    )
  }
  is ChangeGameSettings -> {
    GameState(
      allPlayers = allPlayers,
      playerPool = playerPool,
      roundNumber = round,
      currentPlayer = player,
      gifts = gifts,
      stats = stats,
      settings = settings,
    )
  }
  is EndGame -> {
    val allPlayers = gifts.toList().map { it.key }
    GameState(
      allPlayers = allPlayers,
      playerPool = setOf(),
      roundNumber = allPlayers.size + 1,
      currentPlayer = null,
      gifts = gifts,
      stats = stats,
      settings = GameSettings.Default,
    )
  }
}
