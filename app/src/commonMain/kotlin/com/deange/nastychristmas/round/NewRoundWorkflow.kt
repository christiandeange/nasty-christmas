package com.deange.nastychristmas.round

import com.deange.nastychristmas.round.NewRoundOutput.PlayerSelected
import com.deange.nastychristmas.round.NewRoundOutput.UpdateGameStateWithPlayer
import com.deange.nastychristmas.round.NewRoundState.NextPlayerSelected
import com.deange.nastychristmas.round.NewRoundState.SelectingNextPlayer
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import kotlin.random.Random

class NewRoundWorkflow(
  private val random: Random,
) : StatefulWorkflow<NewRoundProps, NewRoundState, NewRoundOutput, ViewRendering>() {
  override fun initialState(props: NewRoundProps, snapshot: Snapshot?): NewRoundState {
    return NewRoundState.serializer().fromSnapshot(snapshot)
      ?: if (props.selectedPlayer != null) {
        NextPlayerSelected(props.selectedPlayer)
      } else if (props.playerPool.size == 1) {
        NextPlayerSelected(props.playerPool.single())
      } else {
        SelectingNextPlayer
      }
  }

  override fun render(
    renderProps: NewRoundProps,
    renderState: NewRoundState,
    context: RenderContext
  ): ViewRendering = when (renderState) {
    is SelectingNextPlayer -> {
      NewRoundPlayerSelectionScreen(
        random = random,
        playerPool = renderProps.playerPool,
        round = renderProps.roundNumber,
        onPlayerSelected = context.eventHandler { player ->
          state = NextPlayerSelected(player)
          setOutput(UpdateGameStateWithPlayer(player))
        }
      )
    }
    is NextPlayerSelected -> {
      NewRoundPlayerScreen(
        random = random,
        player = renderState.player,
        round = renderProps.roundNumber,
        onContinue = context.eventHandler {
          setOutput(PlayerSelected(renderState.player))
        }
      )
    }
  }

  override fun snapshotState(state: NewRoundState): Snapshot {
    return NewRoundState.serializer().toSnapshot(state)
  }
}
