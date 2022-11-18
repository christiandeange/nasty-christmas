package com.deange.nastychristmas

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.store.preference
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.workflow.AppOutput.ClearGameState
import com.deange.nastychristmas.workflow.AppOutput.Exit
import com.deange.nastychristmas.workflow.AppOutput.SaveGameState
import com.deange.nastychristmas.workflow.AppProps
import com.deange.nastychristmas.workflow.AppProps.NewGame
import com.deange.nastychristmas.workflow.AppProps.RestoredFromSave
import com.deange.nastychristmas.workflow.AppWorkflow
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModelFactory(
  owner: SavedStateRegistryOwner,
  private val storage: PersistentStorage,
  private val appWorkflow: AppWorkflow,
  defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(
    key: String,
    modelClass: Class<T>,
    handle: SavedStateHandle,
  ): T = AppViewModel(handle, storage, appWorkflow) as T
}

class AppViewModel(
  savedState: SavedStateHandle,
  storage: PersistentStorage,
  appWorkflow: AppWorkflow,
) : ViewModel() {
  private val running = Job()

  var game by storage.preference<GameState?>("game-state", null)

  val renderings: StateFlow<ViewRendering> by lazy {
    val restoredGameState: GameState? =
      runCatching { game }
        .onFailure { game = null }
        .getOrNull()

    val initialProps: AppProps = if (restoredGameState == null) {
      NewGame
    } else {
      RestoredFromSave(restoredGameState)
    }

    renderWorkflowIn(
      workflow = appWorkflow,
      scope = viewModelScope,
      props = MutableStateFlow(initialProps),
      savedStateHandle = savedState,
      onOutput = { output ->
        when (output) {
          is Exit -> running.complete()
          is ClearGameState -> game = null
          is SaveGameState -> game = output.gameState
        }
      }
    )
  }

  suspend fun waitForExit() = running.join()
}
