package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Player

data class OpenGiftProps(
  val player: Player,
  val round: Int,
  val giftNames: Set<String>,
  val isReadOnly: Boolean,
)
