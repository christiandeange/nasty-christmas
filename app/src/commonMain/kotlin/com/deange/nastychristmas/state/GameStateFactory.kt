package com.deange.nastychristmas.state

import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.settings.GameSettings
import com.deange.nastychristmas.workflow.AppState
import com.deange.nastychristmas.workflow.AppState.ChangeGameSettings
import com.deange.nastychristmas.workflow.AppState.EndGame
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.deange.nastychristmas.workflow.AppState.OpeningGift
import com.deange.nastychristmas.workflow.AppState.PickingPlayer
import com.deange.nastychristmas.workflow.AppState.StealingRound

fun AppState.asGameState(): GameState? = when (this) {
  is InitializingPlayers -> {
    GameState(
      allPlayers = allPlayers,
      playerPool = allPlayers.toSet(),
      roundNumber = -1,
      currentPlayer = null,
      gifts = GiftOwners(emptyMap()),
      settings = GameSettings(enforceOwnership = true),
    )
  }
  is PickingPlayer -> {
    GameState(
      allPlayers = allPlayers,
      playerPool = playerPool,
      roundNumber = round,
      currentPlayer = selectedPlayer,
      gifts = gifts,
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
      settings = settings,
    )
  }
  is EndGame -> null
}
