package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.Player

data class StealingRoundOutput(
  val playerOpeningGift: Player,
  val gifts: GiftOwners,
)
