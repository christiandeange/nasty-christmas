package com.deange.nastychristmas.round

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.R
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.asMutableState

@OptIn(WorkflowUiExperimentalApi::class)
class OpenGiftScreen(
  val playerName: String,
  val roundNumber: Int,
  val giftName: TextController,
  val onAddGift: (String) -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      Text(
        modifier = Modifier.padding(bottom = 64.dp),
        text = stringResource(R.string.open_gift_round_title, roundNumber, playerName),
        style = MaterialTheme.typography.titleLarge,
      )

      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        var currentGiftName by giftName.asMutableState()

        OutlinedTextField(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(20.dp),
          value = currentGiftName,
          onValueChange = { currentGiftName = it },
          label = { Text(stringResource(R.string.gift_hint)) },
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
        )

        FilledTonalButton(
          modifier = Modifier
            .size(56.dp)
            .align(Alignment.Bottom),
          enabled = currentGiftName.isNotBlank(),
          onClick = { onAddGift(currentGiftName) },
          contentPadding = PaddingValues(0.dp),
        ) {
          Icon(
            painter = rememberVectorPainter(Icons.Default.Add),
            contentDescription = stringResource(R.string.add),
          )
        }
      }
    }
  }
}
