package com.deange.nastychristmas.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults.filledTonalButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.model.OwnedGift
import com.deange.nastychristmas.ui.compose.AppScaffold
import com.deange.nastychristmas.ui.compose.BackBehaviour.Enabled
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.compose.TwoLineText
import com.deange.nastychristmas.ui.compose.asMutableState
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering

class GameSettingsScreen(
  val onBack: () -> Unit,
  val enforceOwnership: Boolean,
  val onEnforceOwnershipChanged: (Boolean) -> Unit,
  val giftNames: List<GiftNameRow>,
  val showConfirmResetGame: Boolean,
  val onResetGame: () -> Unit,
  val onConfirmSettings: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    AppScaffold(
      onBack = Enabled(onBack),
      title = { Text(Strings.settings.evaluate()) },
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(vertical = 16.dp)
      ) {
        EnforceOwnershipCheck(
          enforceOwnership = enforceOwnership,
          onEnforceOwnershipChanged = onEnforceOwnershipChanged,
        )

        Text(
          modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
          text = Strings.editGiftNames.evaluate(),
          style = MaterialTheme.typography.titleMedium,
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
          items(giftNames, key = { "${it.gift.owner.name}-${it.gift.gift.name}" }) { giftName ->
            EditGiftCardNameRow(giftName)
          }
        }

        Column(
          modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
          verticalArrangement = spacedBy(16.dp),
        ) {
          if (!showConfirmResetGame) {
            OutlinedButton(
              modifier = Modifier.fillMaxWidth(),
              onClick = { onResetGame() },
              border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.errorContainer,
              ),
            ) {
              Text(
                style = LocalTextStyle.current.copy(fontWeight = Bold),
                text = Strings.resetApp.evaluate().uppercase(),
              )
            }
          } else {
            FilledTonalButton(
              modifier = Modifier.fillMaxWidth(),
              onClick = { onResetGame() },
              colors = filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
              ),
            ) {
              Text(
                style = LocalTextStyle.current.copy(fontWeight = Bold),
                text = Strings.confirmResetApp.evaluate().uppercase(),
              )
            }
          }

          FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onConfirmSettings() },
          ) {
            Text(
              style = LocalTextStyle.current.copy(fontWeight = Bold),
              text = Strings.confirm.evaluate().uppercase(),
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnforceOwnershipCheck(
  modifier: Modifier = Modifier,
  enforceOwnership: Boolean,
  onEnforceOwnershipChanged: (Boolean) -> Unit,
) {
  Row(
    modifier = modifier
      .clickable { onEnforceOwnershipChanged(!enforceOwnership) }
      .fillMaxWidth()
      .heightIn(min = 56.dp),
    verticalAlignment = CenterVertically,
  ) {
    Checkbox(
      modifier = Modifier.size(56.dp),
      onCheckedChange = { onEnforceOwnershipChanged(!enforceOwnership) },
      checked = enforceOwnership,
    )

    TwoLineText(
      title = Strings.enforceOwnershipTitle.evaluate(),
      description = Strings.enforceOwnershipDescription.evaluate(),
    )
  }
}

@Composable
fun EditGiftCardNameRow(row: GiftNameRow) {
  Row(
    modifier = Modifier.padding(horizontal = 16.dp),
    horizontalArrangement = spacedBy(16.dp),
    verticalAlignment = CenterVertically,
  ) {
    TwoLineText(
      modifier = Modifier.weight(1f),
      title = row.gift.gift.name,
      description = row.gift.owner.name,
    )

    var currentGiftName by row.name.asMutableState()

    OutlinedTextField(
      modifier = Modifier.weight(1f),
      value = currentGiftName,
      onValueChange = { currentGiftName = it },
      singleLine = true,
      keyboardOptions = KeyboardOptions(capitalization = Sentences),
    )
  }
}

data class GiftNameRow(
  val gift: OwnedGift,
  val name: TextController,
)
