package com.deange.nastychristmas.ui.workflow

import androidx.compose.runtime.Composable
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
interface ViewRendering : Screen {
  @Composable
  fun Content()
}
