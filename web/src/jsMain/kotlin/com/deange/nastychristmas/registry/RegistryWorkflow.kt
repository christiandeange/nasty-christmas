package com.deange.nastychristmas.registry

import com.deange.nastychristmas.firebase.Firestore
import com.deange.nastychristmas.firebase.observe
import com.deange.nastychristmas.registry.RegistryState.GameCodeStatus
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.state.GameStateFactory
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.workflow.AppProps.NewGame
import com.deange.nastychristmas.workflow.AppProps.RestoredFromSave
import com.deange.nastychristmas.workflow.AppWorkflow
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.Worker
import com.squareup.workflow1.WorkflowAction.Companion.noAction
import com.squareup.workflow1.action
import com.squareup.workflow1.asWorker
import com.squareup.workflow1.runningWorker

class RegistryWorkflow(
  private val appWorkflow: AppWorkflow,
  private val firestore: Firestore,
  private val gameStateFactory: GameStateFactory,
) : StatefulWorkflow<Unit, RegistryState, Unit, ViewRendering>() {
  override fun initialState(
    props: Unit,
    snapshot: Snapshot?,
  ): RegistryState {
    return RegistryState(
      appState = appWorkflow.initialState(NewGame, snapshot = null),
      gameCode = GameCodeStatus.None,
    )
  }

  override fun render(
    renderProps: Unit,
    renderState: RegistryState,
    context: RenderContext,
  ): ViewRendering {
    val childRendering = when (val gameCode = renderState.gameCode) {
      is GameCodeStatus.None -> {
        ViewRendering.Empty
      }
      is GameCodeStatus.Unvalidated -> {
        val worker = Worker.from { firestore.exists("game-state/${gameCode.code}/") }
        context.runningWorker(worker, "game-state-exists-${gameCode.code}") { exists ->
          action {
            state = if (exists) {
              state.copy(gameCode = GameCodeStatus.Validated(gameCode.code))
            } else {
              state.copy(gameCode = GameCodeStatus.None)
            }
          }
        }
        ViewRendering.Empty
      }
      is GameCodeStatus.Validated -> {
        val worker = firestore.observe<GameState>("game-state/${gameCode.code}/").asWorker()
        context.runningWorker(worker, "game-state-${gameCode.code}") { currentStateData ->
          action {
            state = currentStateData?.appState?.let { state.copy(appState = it) }
              ?: initialState(renderProps, null)
          }
        }

        val gameState = gameStateFactory.create(renderState.appState)
        context.renderChild(appWorkflow, RestoredFromSave(gameState, isReadOnly = true)) { noAction() }
      }
    }

    return RegistryScreen(
      gameCode = renderState.gameCode,
      onGameCodeEntered = context.eventHandler { newGameCode ->
        state = state.copy(gameCode = GameCodeStatus.Unvalidated(newGameCode))
      },
      onClearGameCode = context.eventHandler {
        state = state.copy(gameCode = GameCodeStatus.None)
      },
      childRendering = childRendering,
    )
  }

  override fun snapshotState(state: RegistryState): Snapshot? = null
}
