package com.deange.nastychristmas.ui.workflow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T : Any> WorkflowRenderings(
  renderings: StateFlow<T>,
  key: Any = renderings,
  content: @Composable (T) -> Unit,
) {
  val maybeRendering: T? by produceState<T?>(null, key) {
    renderings.collect { value = it }
  }

  maybeRendering?.let { rendering ->
    content(rendering)
  }
}
