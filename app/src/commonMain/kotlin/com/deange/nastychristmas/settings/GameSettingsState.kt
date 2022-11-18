package com.deange.nastychristmas.settings

import com.deange.nastychristmas.model.OwnedGift
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.workflow.TextControllerSerializer
import kotlinx.serialization.Serializable

@Serializable
data class GameSettingsState(
  val enforceOwnership: Boolean,
  val showConfirmResetGame: Boolean,
  val giftNames: List<GiftName>,
)

@Serializable
data class GiftName(
  val gift: OwnedGift,
  @Serializable(with = TextControllerSerializer::class)
  val newName: TextController,
)
