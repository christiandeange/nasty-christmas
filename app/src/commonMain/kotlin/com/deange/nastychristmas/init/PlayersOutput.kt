package com.deange.nastychristmas.init

import com.deange.nastychristmas.model.Player

sealed class PlayersOutput {
  data object NoPlayers : PlayersOutput()

  data class PlayersUpdated(
    val players: List<Player>,
  ) : PlayersOutput()

  data class GameCodeUpdated(
    val gameCode: String,
  ) : PlayersOutput()

  data class StartWithPlayers(
    val players: List<Player>,
  ) : PlayersOutput()
}
