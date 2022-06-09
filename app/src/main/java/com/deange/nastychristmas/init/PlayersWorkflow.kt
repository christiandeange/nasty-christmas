package com.deange.nastychristmas.init

import com.deange.nastychristmas.init.PlayersOutput.NoPlayers
import com.deange.nastychristmas.init.PlayersOutput.StartWithPlayers
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.ui.TextController

class PlayersWorkflow : StatefulWorkflow<Unit, PlayersState, PlayersOutput, PlayersScreen>() {
  override fun initialState(props: Unit, snapshot: Snapshot?): PlayersState {
    return PlayersState.serializer().fromSnapshot(snapshot)
      ?: PlayersState(
        players = emptyList(),
        currentPlayer = TextController(""),
      )
  }

  override fun render(
    renderProps: Unit,
    renderState: PlayersState,
    context: RenderContext
  ): PlayersScreen {
    return PlayersScreen(
      players = renderState.players.map { it.name },
      currentPlayer = renderState.currentPlayer,
      onAddPlayer = context.eventHandler { name ->
        val newPlayers = state.players + Player(name.trim())
        state = state.copy(
          players = newPlayers,
          // Clear the current player name.
          currentPlayer = TextController(""),
        )
      },
      onDeletePlayer = context.eventHandler { index ->
        val newPlayers = state.players.toMutableList().apply { removeAt(index) }.toList()
        state = state.copy(players = newPlayers)
      },
      onBack = context.eventHandler {
        setOutput(NoPlayers)
      },
      onStartGame = context.eventHandler {
        setOutput(StartWithPlayers(state.players))
      },
    )
  }

  override fun snapshotState(state: PlayersState): Snapshot {
    return PlayersState.serializer().toSnapshot(state)
  }
}
