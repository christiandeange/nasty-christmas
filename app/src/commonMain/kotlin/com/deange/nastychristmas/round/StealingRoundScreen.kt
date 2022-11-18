package com.deange.nastychristmas.round

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.core.MR
import com.deange.nastychristmas.round.StealOrOpenChoice.Open
import com.deange.nastychristmas.round.StealOrOpenChoice.Steal
import com.deange.nastychristmas.ui.compose.AppScaffold
import com.deange.nastychristmas.ui.compose.BackBehaviour
import com.deange.nastychristmas.ui.compose.TwoLineText
import com.deange.nastychristmas.ui.compose.evaluate
import com.deange.nastychristmas.ui.workflow.ViewRendering

@OptIn(ExperimentalFoundationApi::class)
class StealingRoundScreen(
  val playerName: String,
  val roundNumber: Int,
  val choices: List<StealOrOpenChoice>,
  val onUndo: (() -> Unit)?,
  val onConfirmChoice: () -> Unit,
  val onChangeSettings: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    val backBehaviour = if (onUndo != null) {
      BackBehaviour.Enabled(onUndo)
    } else {
      BackBehaviour.Disabled
    }

    AppScaffold(
      onBack = backBehaviour,
      title = {
        TwoLineText(
          title = MR.strings.round.evaluate(roundNumber),
          description = MR.strings.steal_round_title.evaluate(playerName),
        )
      },
      actionIcons = {
        IconButton(onClick = { onChangeSettings() }) {
          Icon(
            painter = rememberVectorPainter(image = Icons.Default.Settings),
            contentDescription = MR.strings.settings.evaluate(),
          )
        }
      }
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(vertical = 16.dp)
      ) {
        LazyVerticalGrid(
          modifier = Modifier.weight(1f),
          columns = GridCells.Adaptive(minSize = 256.dp),
        ) {
          items(choices, key = { it.key }) { choice ->
            when (choice) {
              is Open -> {
                ChoiceRow(
                  modifier = Modifier.animateItemPlacement(),
                  titleText = MR.strings.open_gift_title.evaluate(),
                  descriptionText = MR.strings.open_gift_description.evaluate(),
                  isSelected = choice.isSelected,
                  onClick = choice.onPicked,
                )
              }
              is Steal -> {
                ChoiceRow(
                  modifier = Modifier.animateItemPlacement(),
                  titleText = MR.strings.steal_from.evaluate(choice.playerName),
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
            text = MR.strings.confirm.evaluate().uppercase(),
          )
        }
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
    override val key: String = "Steal($giftName)"
  }
}
