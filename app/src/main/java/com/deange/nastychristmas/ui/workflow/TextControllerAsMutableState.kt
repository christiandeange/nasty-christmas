package com.deange.nastychristmas.ui.workflow

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Exposes the [textValue][TextController.textValue] of a [TextController]
 * as a remembered [MutableState], suitable for use from `@Composable`
 * functions.
 *
 * Usage:
 *
 *    var text by rendering.textController.asMutableState()
 *
 *    OutlinedTextField(
 *      label = {},
 *      placeholder = { Text("Enter some text") },
 *      value = text,
 *      onValueChange = { text = it }
 *    )
 */
@Composable
fun TextController.asMutableState(): MutableState<String> {
  // keys are set to `this` to reset the state if a different controller is passed in…
  return remember(this) { mutableStateOf(textValue) }.also { state ->
    // …and to restart the effect.
    LaunchedEffect(this) {
      // Push changes from the workflow to the state.
      launch {
        onTextChanged.collect { state.value = it }
      }
      // And the other way – push changes to the state to the workflow.
      // This won't cause an infinite loop because both MutableState and
      // MutableSnapshotFlow ignore duplicate values.
      snapshotFlow { state.value }.collect { textValue = it }
    }
  }
}
