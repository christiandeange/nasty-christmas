@file:OptIn(WorkflowUiExperimentalApi::class)

package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.ui.workflow.*
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.renderChild
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BodyAndModalsScreen

typealias AppScreen = BodyAndModalsScreen<ViewRendering, BottomSheetScreen>

class AppWorkflow(
  private val playersWorkflow: PlayersWorkflow,
) : StatefulWorkflow<Unit, AppState, Unit, AppScreen>() {
  override fun initialState(props: Unit, snapshot: Snapshot?): AppState {
    return AppState.serializer().fromSnapshot(snapshot)
      ?: InitializingPlayers
  }

  override fun render(
    renderProps: Unit,
    renderState: AppState,
    context: RenderContext
  ): AppScreen {
    return BodyAndModalsScreen(
      body = context.renderChild(playersWorkflow) {
        action {
          setOutput(Unit)
        }
      }
    )
  }

  override fun snapshotState(state: AppState): Snapshot {
    return when (state) {
      is InitializingPlayers -> InitializingPlayers.serializer().toSnapshot(state)
    }
  }
}
