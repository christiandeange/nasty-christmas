package com.deange.nastychristmas.round

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.R
import com.deange.nastychristmas.round.StealOrOpenChoice.Open
import com.deange.nastychristmas.round.StealOrOpenChoice.Steal
import com.deange.nastychristmas.ui.compose.TwoLineText
import com.deange.nastychristmas.ui.workflow.ViewRendering

@OptIn(ExperimentalFoundationApi::class)
class StealingRoundScreen(
  val playerName: String,
  val roundNumber: Int,
  val choices: List<StealOrOpenChoice>,
  val onConfirmChoice: () -> Unit,
  val onChangeSettings: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 16.dp)
    ) {
      Row(
        modifier = Modifier.padding(bottom = 64.dp),
        horizontalArrangement = spacedBy(16.dp),
        verticalAlignment = CenterVertically,
      ) {
        Text(
          modifier = Modifier
            .padding(start = 16.dp)
            .weight(1f),
          text = stringResource(R.string.steal_round_title, roundNumber, playerName),
          style = MaterialTheme.typography.titleLarge,
        )

        IconButton(onClick = { onChangeSettings() }) {
          Icon(
            painter = rememberVectorPainter(image = Icons.Default.Settings),
            contentDescription = stringResource(R.string.settings),
          )
        }
      }

      LazyColumn(modifier = Modifier.weight(1f)) {
        items(choices, key = { it.key }) { choice ->
          when (choice) {
            is Open -> {
              ChoiceRow(
                modifier = Modifier.animateItemPlacement(),
                titleText = stringResource(R.string.open_gift_title),
                descriptionText = stringResource(R.string.open_gift_description),
                isSelected = choice.isSelected,
                onClick = choice.onPicked,
              )
            }
            is Steal -> {
              ChoiceRow(
                modifier = Modifier.animateItemPlacement(),
                titleText = stringResource(R.string.steal_from, choice.playerName),
                descriptionText = choice.giftName,
                isSelected = choice.isSelected,
                onClick = choice.onPicked.takeIf { choice.isEnabled },
              )
            }
          }
        }
      }

      FilledTonalButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        enabled = choices.any { it.isSelected },
        onClick = { onConfirmChoice() },
      ) {
        Text(
          style = LocalTextStyle.current.copy(fontWeight = Bold),
          text = stringResource(R.string.confirm).uppercase(),
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceRow(
  modifier: Modifier = Modifier,
  titleText: String,
  descriptionText: String,
  isSelected: Boolean,
  onClick: (() -> Unit)?,
) {
  val clickableModifier = if (onClick != null) {
    Modifier.clickable { onClick() }
  } else {
    Modifier
  }

  val textColor = if (onClick != null) {
    LocalTextStyle.current.color
  } else {
    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(min = 56.dp)
      .then(clickableModifier),
    verticalAlignment = CenterVertically,
  ) {
    RadioButton(
      modifier = Modifier.size(56.dp),
      onClick = onClick,
      enabled = onClick != null,
      selected = isSelected,
    )

    TwoLineText(
      title = titleText,
      description = descriptionText,
      textColor = textColor,
    )
  }
}

sealed class StealOrOpenChoice {
  abstract val isSelected: Boolean
  abstract val onPicked: () -> Unit

  abstract val key: String

  class Open(
    override val isSelected: Boolean,
    override val onPicked: () -> Unit,
  ) : StealOrOpenChoice() {
    override val key: String = "Open"
  }

  class Steal(
    val playerName: String,
    val giftName: String,
    val isEnabled: Boolean,
    override val isSelected: Boolean,
    override val onPicked: () -> Unit,
  ) : StealOrOpenChoice() {
    override val key: String = "Steal($playerName-$giftName)"
  }
}
