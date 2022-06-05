package com.deange.nastychristmas.ui.workflow

abstract class BottomSheetScreen: ViewRendering {
  /**
   * Will be invoked on back press, tapping outside dialog, or swiping the * bottom sheet dialog
   * down to the hidden state.
   */
  abstract val onCancel: () -> Unit
}
