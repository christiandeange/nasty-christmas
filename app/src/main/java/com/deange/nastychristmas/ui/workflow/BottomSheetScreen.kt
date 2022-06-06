package com.deange.nastychristmas.ui.workflow

import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.Overlay

@OptIn(WorkflowUiExperimentalApi::class)
abstract class BottomSheetScreen: ViewRendering, Overlay {
  /**
   * Will be invoked on back press, tapping outside dialog, or swiping the
   * bottom sheet dialog down to the hidden state.
   */
  abstract val onCancel: () -> Unit
}
