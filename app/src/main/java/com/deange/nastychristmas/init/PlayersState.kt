package com.deange.nastychristmas.init

import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.ui.workflow.TextControllerSerializer
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import kotlinx.serialization.Serializable

@OptIn(WorkflowUiExperimentalApi::class)
@Serializable
data class PlayersState(
  val players: List<Player>,
  @Serializable(with = TextControllerSerializer::class)
  val currentPlayer: TextController,
)
