package com.deange.nastychristmas.ui.compose

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive

@Composable
fun GridStateAutoScrollEffect(state: GridAutoScrollState) {
  LaunchedEffect(Unit) {
    var direction = 1f

    while (isActive) {
      // Wait for auto-scrolling to be enabled
      state.isEnabledState.first { it }

      while (state.isEnabled) {
        val magnitude = state.scrollSpeed.value
        val scrollAmount = direction * magnitude

        val scrolledBy = state.scrollBy(scrollAmount)

        if (scrolledBy != scrollAmount) {
          direction = -direction
          delay(2000 - 250L * magnitude)
        } else {
          delay(10)
        }
      }
    }
  }
}

data class GridAutoScrollState(
  private val gridState: LazyGridState,
  private val autoScrollSpeed: Int,
) {
  internal val isEnabledState = MutableStateFlow(false)
  internal val isEnabled: Boolean get() = isEnabledState.value

  val scrollSpeed: MutableState<Int> = mutableStateOf(autoScrollSpeed)

  suspend fun start() {
    isEnabledState.emit(true)
  }

  suspend fun pause() {
    isEnabledState.emit(false)
  }

  suspend fun scrollBy(value: Float): Float {
    return gridState.scrollBy(value)
  }
}

@Composable
fun rememberGridAutoScrollState(
  gridState: LazyGridState,
  autoScrollSpeed: Int = 1,
): GridAutoScrollState {
  return remember {
    GridAutoScrollState(gridState, autoScrollSpeed)
  }
}
