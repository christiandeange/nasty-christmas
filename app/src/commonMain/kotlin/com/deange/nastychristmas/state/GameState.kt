package com.deange.nastychristmas.state

import com.deange.nastychristmas.workflow.AppState
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
  val appState: AppState,
  @Serializable(with = InstantIso8601Serializer::class)
  val updatedAt: Instant,
)
