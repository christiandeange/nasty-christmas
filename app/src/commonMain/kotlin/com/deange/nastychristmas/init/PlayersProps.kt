package com.deange.nastychristmas.init

import com.deange.nastychristmas.model.Player

data class PlayersProps(
  val players: List<Player>,
  val isReadOnly: Boolean,
  val gameCode: String?,
)
