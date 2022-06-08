package com.deange.nastychristmas.storage

import com.deange.nastychristmas.workflow.AppState
import com.deange.nastychristmas.workflow.AppState.ChangeGameSettings
import com.deange.nastychristmas.workflow.AppState.EndGame
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.deange.nastychristmas.workflow.AppState.OpeningGift
import com.deange.nastychristmas.workflow.AppState.PickingPlayer
import com.deange.nastychristmas.workflow.AppState.StealingRound

fun AppState.asGameState(): GameState? = when (this) {
  is InitializingPlayers -> null
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
