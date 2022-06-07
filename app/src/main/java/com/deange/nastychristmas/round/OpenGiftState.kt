package com.deange.nastychristmas.round

import com.deange.nastychristmas.ui.workflow.TextControllerSerializer
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import kotlinx.serialization.Serializable

@OptIn(WorkflowUiExperimentalApi::class)
@Serializable
data class OpenGiftState(
  @Serializable(with = TextControllerSerializer::class)
  val giftName: TextController
)
