package com.deange.nastychristmas.end

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners

data class EndGameProps(
  val finalGifts: GiftOwners,
  val stats: GameStats,
)
