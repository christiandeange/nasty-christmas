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

class NewRoundWorkflow : StatefulWorkflow<NewRoundProps, NewRoundState, NewRoundOutput, ViewRendering>() {
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

  override fun onPropsChanged(old: NewRoundProps, new: NewRoundProps, state: NewRoundState): NewRoundState {
    return initialState(new, snapshot = null)
  }

  override fun render(
    renderProps: NewRoundProps,
    renderState: NewRoundState,
    context: RenderContext<NewRoundProps, NewRoundState, NewRoundOutput>,
  ): ViewRendering = when (renderState) {
    is SelectingNextPlayer -> {
      NewRoundPlayerSelectionScreen(
        random = Random(renderProps.seed),
        gameCode = renderProps.settings.gameCode,
        playerPool = renderProps.playerPool,
        round = renderProps.roundNumber,
        onPlayerSelected = context.eventHandler("onPlayerSelected") { player ->
          if (!renderProps.isReadOnly) {
            state = NextPlayerSelected(player)
            setOutput(UpdateGameStateWithPlayer(player))
          }
        }
      )
    }
    is NextPlayerSelected -> {
      NewRoundPlayerScreen(
        random = Random(renderProps.seed),
        gameCode = renderProps.settings.gameCode,
        player = renderState.player,
        round = renderProps.roundNumber,
        onContinue = context.eventHandler("onContinue") {
          if (!renderProps.isReadOnly) {
            setOutput(PlayerSelected(renderState.player))
          }
        }.takeUnless { renderProps.isReadOnly },
      )
    }
  }

  override fun snapshotState(state: NewRoundState): Snapshot {
    return NewRoundState.serializer().toSnapshot(state)
  }
}
