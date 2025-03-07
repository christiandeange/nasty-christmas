package com.deange.nastychristmas.init

import com.deange.nastychristmas.init.PlayersOutput.GameCodeUpdated
import com.deange.nastychristmas.init.PlayersOutput.NoPlayers
import com.deange.nastychristmas.init.PlayersOutput.PlayersUpdated
import com.deange.nastychristmas.init.PlayersOutput.StartWithPlayers
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.state.GameSaver
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.Worker
import com.squareup.workflow1.action
import com.squareup.workflow1.runningWorker

class PlayersWorkflow(
  private val gameSaver: GameSaver,
) : StatefulWorkflow<PlayersProps, PlayersState, PlayersOutput, ViewRendering>() {

  override fun initialState(props: PlayersProps, snapshot: Snapshot?): PlayersState {
    return PlayersState.serializer().fromSnapshot(snapshot)
      ?: PlayersState(
        players = props.players,
        currentPlayer = TextController(""),
        gameCode = props.gameCode,
      )
  }

  override fun onPropsChanged(old: PlayersProps, new: PlayersProps, state: PlayersState): PlayersState {
    return initialState(new, snapshot = null).copy(gameCode = state.gameCode)
  }

  override fun render(
    renderProps: PlayersProps,
    renderState: PlayersState,
    context: RenderContext
  ): ViewRendering {
    if (!renderProps.isReadOnly && renderState.gameCode == null) {
      context.runningWorker(Worker.from { gameSaver.generateGameCode() }, "generate-game-code") { gameCode ->
        action {
          state = state.copy(gameCode = gameCode)
          setOutput(GameCodeUpdated(gameCode))
        }
      }
    }

    return PlayersScreen(
      players = renderState.players.map { it.name },
      currentPlayer = renderState.currentPlayer,
      gameCode = renderState.gameCode,
      isReadOnly = renderProps.isReadOnly,
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
