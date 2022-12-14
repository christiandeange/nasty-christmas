package com.deange.nastychristmas.round

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.ui.compose.BackHandler
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.compose.asMutableState
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering

class OpenGiftScreen(
  val playerName: String,
  val roundNumber: Int,
  val giftName: TextController,
  val errorGiftExists: Boolean,
  val onAddGift: (String) -> Unit,
) : ViewRendering {
  private val originalGiftName = giftName.textValue

  @Composable
  override fun Content() {
    BackHandler(onBack = { /* no-op */ })

    Row(
      modifier = Modifier.padding(16.dp),
      horizontalArrangement = spacedBy(16.dp),
      verticalAlignment = Bottom,
    ) {
      var currentGiftName by giftName.asMutableState()
      var isError by remember(this@OpenGiftScreen, originalGiftName) {
        mutableStateOf(errorGiftExists)
      }

      OutlinedTextField(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(20.dp),
        value = currentGiftName,
        onValueChange = {
          isError = false
          currentGiftName = it
        },
        label = {
          if (isError) {
            Text(Strings.errorGiftExists.evaluate())
          } else {
            Text(Strings.openGiftRoundTitle.evaluate(roundNumber, playerName))
          }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Sentences,
          imeAction = ImeAction.Send,
        ),
        keyboardActions = KeyboardActions(onSend = {
          if (currentGiftName.isNotBlank()) {
            onAddGift(currentGiftName)
          }
        }),
        isError = isError,
      )

      FilledTonalButton(
        modifier = Modifier.size(56.dp),
        enabled = currentGiftName.isNotBlank(),
        onClick = { onAddGift(currentGiftName) },
        contentPadding = PaddingValues(0.dp),
      ) {
        Icon(
          painter = rememberVectorPainter(Icons.Default.Add),
          contentDescription = Strings.add.evaluate(),
        )
      }
    }
  }
}
