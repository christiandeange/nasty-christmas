package com.deange.nastychristmas.init

import com.deange.nastychristmas.model.Player

sealed class PlayersOutput {
  object NoPlayers : PlayersOutput()

  data class StartWithPlayers(
    val players: List<Player>,
  ) : PlayersOutput()
}
