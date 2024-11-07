package com.deange.nastychristmas.registry

import com.deange.nastychristmas.firebase.Firestore
import com.deange.nastychristmas.firebase.observe
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.workflow.AppProps
import com.deange.nastychristmas.workflow.AppState
import com.deange.nastychristmas.workflow.AppWorkflow
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction.Companion.noAction
import com.squareup.workflow1.action
import com.squareup.workflow1.asWorker
import com.squareup.workflow1.runningWorker

class RegistryWorkflow(
  private val appWorkflow: AppWorkflow,
  firestore: Firestore,
) : StatefulWorkflow<Unit, AppState, Unit, ViewRendering>() {
  private val gameStateWorker = firestore.observe<GameState>("game-state/default/").asWorker()

  override fun initialState(
    props: Unit,
    snapshot: Snapshot?,
  ): AppState {
    return appWorkflow.initialState(AppProps.NewGame, snapshot = null)
  }

  override fun render(
    renderProps: Unit,
    renderState: AppState,
    context: RenderContext,
  ): ViewRendering {
    context.runningWorker(gameStateWorker) { currentStateData ->
      action {
        state = currentStateData?.appState ?: initialState(renderProps, null)
      }
    }

    return context.renderChild(appWorkflow, AppProps.RestoredFromSave(GameState(renderState), isReadOnly = true)) {
      noAction()
    }
  }

  override fun snapshotState(state: AppState): Snapshot? = null
}
