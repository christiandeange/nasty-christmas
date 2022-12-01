package com.deange.nastychristmas.init

import com.deange.nastychristmas.init.PlayersOutput.NoPlayers
import com.deange.nastychristmas.init.PlayersOutput.PlayersUpdated
import com.deange.nastychristmas.init.PlayersOutput.StartWithPlayers
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

class PlayersWorkflow :
  StatefulWorkflow<PlayersProps, PlayersState, PlayersOutput, PlayersScreen>() {

  override fun initialState(props: PlayersProps, snapshot: Snapshot?): PlayersState {
    return PlayersState.serializer().fromSnapshot(snapshot)
      ?: PlayersState(
        players = props.players,
        currentPlayer = TextController(""),
      )
  }

  override fun render(
    renderProps: PlayersProps,
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
        setOutput(PlayersUpdated(state.players))
      },
      onDeletePlayer = context.eventHandler { index ->
        val newPlayers = state.players.toMutableList().apply { removeAt(index) }.toList()
        state = state.copy(players = newPlayers)
        setOutput(PlayersUpdated(state.players))
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
