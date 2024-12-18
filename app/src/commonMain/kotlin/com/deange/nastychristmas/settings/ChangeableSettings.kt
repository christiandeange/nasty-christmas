package com.deange.nastychristmas.settings

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners
import kotlinx.serialization.Serializable

@Serializable
data class ChangeableSettings(
  val settings: GameSettings,
  val gifts: GiftOwners,
  val stats: GameStats,
  val roundStats: GameStats,
)
