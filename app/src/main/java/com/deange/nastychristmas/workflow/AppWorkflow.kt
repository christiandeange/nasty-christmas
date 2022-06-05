package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.ui.workflow.BottomSheetContainerScreen
import com.deange.nastychristmas.ui.workflow.BottomSheetScreen
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

typealias AppScreen = BottomSheetContainerScreen<ViewRendering, BottomSheetScreen>

class AppWorkflow : StatefulWorkflow<Unit, AppState, Unit, AppScreen>() {
  override fun initialState(props: Unit, snapshot: Snapshot?): AppState {
    return AppState("Android")
  }

  override fun render(
    renderProps: Unit,
    renderState: AppState,
    context: RenderContext
  ): AppScreen {
    return BottomSheetContainerScreen(
      GreetingScreen(renderState.text)
    )
  }

  override fun snapshotState(state: AppState): Snapshot? = null
}
