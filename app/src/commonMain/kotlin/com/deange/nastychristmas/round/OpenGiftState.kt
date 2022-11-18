package com.deange.nastychristmas.round

import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.workflow.TextControllerSerializer
import kotlinx.serialization.Serializable

@Serializable
data class OpenGiftState(
  @Serializable(with = TextControllerSerializer::class)
  val giftName: TextController
)
