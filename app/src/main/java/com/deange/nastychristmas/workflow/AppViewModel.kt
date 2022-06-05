package com.deange.nastychristmas.workflow

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModelFactory(
  owner: SavedStateRegistryOwner,
  private val appWorkflow: AppWorkflow,
  defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(
    key: String,
    modelClass: Class<T>,
    handle: SavedStateHandle,
  ): T = AppViewModel(handle, appWorkflow) as T
}

class AppViewModel(
  savedState: SavedStateHandle,
  appWorkflow: AppWorkflow,
) : ViewModel() {
  private val running = Job()

  @OptIn(WorkflowUiExperimentalApi::class)
  val renderings: StateFlow<AppScreen> by lazy {
    renderWorkflowIn(
      workflow = appWorkflow,
      scope = viewModelScope,
      props = MutableStateFlow(Unit),
      savedStateHandle = savedState,
      onOutput = { running.complete() }
    )
  }

  suspend fun waitForExit() = running.join()
}
