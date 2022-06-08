package com.deange.nastychristmas.workflow

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.deange.nastychristmas.storage.GameState
import com.deange.nastychristmas.storage.GameStateStorage
import com.deange.nastychristmas.workflow.AppOutput.ClearGameState
import com.deange.nastychristmas.workflow.AppOutput.Exit
import com.deange.nastychristmas.workflow.AppOutput.SaveGameState
import com.deange.nastychristmas.workflow.AppProps.NewGame
import com.deange.nastychristmas.workflow.AppProps.RestoredFromSave
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppViewModelFactory(
  owner: SavedStateRegistryOwner,
  private val storage: GameStateStorage,
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
  storage: GameStateStorage,
  appWorkflow: AppWorkflow,
) : ViewModel() {
  private val running = Job()

  @OptIn(WorkflowUiExperimentalApi::class)
  val renderings: StateFlow<AppScreen> by lazy {
    val restoredGameState: GameState? = runBlocking { storage.get() }
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
          is Exit -> {
            running.complete()
          }
          is ClearGameState -> {
            viewModelScope.launch { storage.clear() }
          }
          is SaveGameState -> {
            viewModelScope.launch { storage.set(output.gameState) }
          }
        }
      }
    )
  }

  suspend fun waitForExit() = running.join()
}
