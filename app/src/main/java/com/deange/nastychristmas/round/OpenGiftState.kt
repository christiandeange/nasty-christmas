package com.deange.nastychristmas.round

import com.deange.nastychristmas.ui.workflow.TextControllerSerializer
import com.squareup.workflow1.ui.TextController
import kotlinx.serialization.Serializable

@Serializable
data class OpenGiftState(
  @Serializable(with = TextControllerSerializer::class)
  val giftName: TextController
)
