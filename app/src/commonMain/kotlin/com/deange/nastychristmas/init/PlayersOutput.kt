package com.deange.nastychristmas.init

import com.deange.nastychristmas.model.Player

sealed class PlayersOutput {
  object NoPlayers : PlayersOutput()

  data class PlayersUpdated(
    val players: List<Player>,
  ) : PlayersOutput()

  data class StartWithPlayers(
    val players: List<Player>,
  ) : PlayersOutput()
}
