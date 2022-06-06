@file:OptIn(WorkflowUiExperimentalApi::class)

package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.init.PlayersOutput.NoPlayers
import com.deange.nastychristmas.init.PlayersOutput.StartWithPlayers
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.round.NewRoundProps
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.ui.workflow.BottomSheetScreen
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.deange.nastychristmas.workflow.AppState.PickingPlayer
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.renderChild
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BodyAndModalsScreen

typealias AppScreen = BodyAndModalsScreen<ViewRendering, BottomSheetScreen>

class AppWorkflow(
  private val playersWorkflow: PlayersWorkflow,
  private val newRoundWorkflow: NewRoundWorkflow,
) : StatefulWorkflow<Unit, AppState, Unit, AppScreen>() {
  override fun initialState(props: Unit, snapshot: Snapshot?): AppState {
    return AppState.serializer().fromSnapshot(snapshot)
      ?: InitializingPlayers
  }

  override fun render(
    renderProps: Unit,
    renderState: AppState,
    context: RenderContext
  ): AppScreen = when (renderState) {
    is InitializingPlayers -> {
      BodyAndModalsScreen(
        body = context.renderChild(playersWorkflow) { output ->
          action {
            when (output) {
              is NoPlayers -> setOutput(Unit)
              is StartWithPlayers -> {
                state = PickingPlayer(
                  allPlayers = output.players,
                  playerPool = output.players.toSet(),
                  round = 1,
                )
              }
            }
          }
        }
      )
    }
    is PickingPlayer -> {
      val newRoundProps = NewRoundProps(
        allPlayers = renderState.allPlayers,
        playerPool = renderState.playerPool,
        roundNumber = renderState.round,
      )
      BodyAndModalsScreen(
        body = context.renderChild(newRoundWorkflow, newRoundProps) { output ->
          action {
            setOutput(Unit)
          }
        }
      )
    }
  }

  override fun snapshotState(state: AppState): Snapshot {
    return AppState.serializer().toSnapshot(state)
  }
}
