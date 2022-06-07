package com.deange.nastychristmas.settings

import com.deange.nastychristmas.model.Gift
import com.deange.nastychristmas.model.OwnedGift
import com.deange.nastychristmas.ui.workflow.TextControllerSerializer
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import kotlinx.serialization.Serializable

@Serializable
data class GameSettingsState(
  val enforceOwnership: Boolean,
  val giftNames: List<GiftName>,
)

@OptIn(WorkflowUiExperimentalApi::class)
@Serializable
data class GiftName(
  val gift: OwnedGift,
  @Serializable(with = TextControllerSerializer::class)
  val newName: TextController,
)
