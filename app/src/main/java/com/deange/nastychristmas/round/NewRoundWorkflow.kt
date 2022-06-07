package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.round.NewRoundState.NextPlayerSelected
import com.deange.nastychristmas.round.NewRoundState.SelectingNextPlayer
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

class NewRoundWorkflow :
  StatefulWorkflow<NewRoundProps, NewRoundState, Player, ViewRendering>() {
  override fun initialState(props: NewRoundProps, snapshot: Snapshot?): NewRoundState {
    return NewRoundState.serializer().fromSnapshot(snapshot)
      ?: if (props.playerPool.size > 1) {
        SelectingNextPlayer
      } else {
        NextPlayerSelected(props.playerPool.single())
      }
  }

  override fun render(
    renderProps: NewRoundProps,
    renderState: NewRoundState,
    context: RenderContext
  ): ViewRendering = when (renderState) {
    is SelectingNextPlayer -> {
      NewRoundPlayerSelectionScreen(
        playerPool = renderProps.playerPool,
        round = renderProps.roundNumber,
        onPlayerSelected = context.eventHandler { player ->
          state = NextPlayerSelected(player)
        }
      )
    }
    is NextPlayerSelected -> {
      NewRoundPlayerScreen(
        player = renderState.player,
        round = renderProps.roundNumber,
        onContinue = context.eventHandler {
          setOutput(renderState.player)
        }
      )
    }
  }

  override fun snapshotState(state: NewRoundState): Snapshot {
    return NewRoundState.serializer().toSnapshot(state)
  }
}
