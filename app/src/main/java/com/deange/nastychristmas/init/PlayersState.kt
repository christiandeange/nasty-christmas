package com.deange.nastychristmas.init

import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.ui.workflow.TextControllerSerializer
import com.squareup.workflow1.ui.TextController
import kotlinx.serialization.Serializable

@Serializable
data class PlayersState(
  val players: List<Player>,
  @Serializable(with = TextControllerSerializer::class)
  val currentPlayer: TextController,
)
