package com.deange.nastychristmas.registry

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.deange.nastychristmas.registry.RegistryState.GameCodeStatus
import com.deange.nastychristmas.ui.compose.AppScaffold
import com.deange.nastychristmas.ui.compose.BackBehaviour
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering

class RegistryScreen(
  private val gameCode: GameCodeStatus,
  private val onGameCodeEntered: (String) -> Unit,
  private val onClearGameCode: () -> Unit,
  private val childRendering: ViewRendering,
) : ViewRendering {
  @Composable
  override fun Content() {
    AppScaffold(
      onBack = BackBehaviour.Hidden,
      title = {
        when (gameCode) {
          is GameCodeStatus.None,
          is GameCodeStatus.Unvalidated -> {
            var currentGameCode by remember { mutableStateOf("") }
            val isEnabled = currentGameCode.length == 6 && gameCode is GameCodeStatus.None

            OutlinedTextField(
              shape = CircleShape,
              value = currentGameCode,
              onValueChange = { currentGameCode = it.take(6).uppercase() },
              label = { Text(Strings.enterGameCode.evaluate()) },
              keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                autoCorrectEnabled = false,
                imeAction = Send,
              ),
              keyboardActions = KeyboardActions(onSend = {
                if (isEnabled) {
                  onGameCodeEntered(currentGameCode)
                }
              }),
              trailingIcon = {
                IconButton(
                  onClick = { onGameCodeEntered(currentGameCode) },
                  enabled = isEnabled,
                  content = {
                    Icon(
                      painter = rememberVectorPainter(Icons.AutoMirrored.Filled.Send),
                      contentDescription = Strings.add.evaluate(),
                    )
                  }
                )
              }
            )
          }
          is GameCodeStatus.Validated -> {
            Text(gameCode.code)
          }
        }
      },
      actionIcons = {
        if (gameCode is GameCodeStatus.Validated) {
          IconButton(
            onClick = onClearGameCode,
            content = {
              Icon(
                painter = rememberVectorPainter(Icons.Default.Clear),
                contentDescription = Strings.clear.evaluate(),
              )
            }
          )
        }
      },
      content = {
        childRendering.Content()
      }
    )
  }
}
