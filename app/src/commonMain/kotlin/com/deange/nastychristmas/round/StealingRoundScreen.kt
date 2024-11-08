package com.deange.nastychristmas.round

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.round.StealOrOpenChoice.Open
import com.deange.nastychristmas.round.StealOrOpenChoice.Steal
import com.deange.nastychristmas.ui.compose.AppScaffold
import com.deange.nastychristmas.ui.compose.BackBehaviour
import com.deange.nastychristmas.ui.compose.GridStateAutoScrollEffect
import com.deange.nastychristmas.ui.compose.TwoLineText
import com.deange.nastychristmas.ui.compose.rememberGridAutoScrollState
import com.deange.nastychristmas.ui.icons.LooksOne
import com.deange.nastychristmas.ui.icons.LooksThree
import com.deange.nastychristmas.ui.icons.LooksTwo
import com.deange.nastychristmas.ui.icons.LooksZero
import com.deange.nastychristmas.ui.icons.Visibility
import com.deange.nastychristmas.ui.icons.VisibilityOff
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering
import kotlinx.coroutines.delay
import kotlin.time.TimeSource.Monotonic.markNow

@OptIn(ExperimentalFoundationApi::class)
class StealingRoundScreen(
  val playerName: String,
  val roundNumber: Int,
  val isLastRound: Boolean,
  val choices: List<StealOrOpenChoice>,
  val showUnstealableGifts: Boolean,
  val autoScrollSpeed: Int,
  val isReadOnly: Boolean,
  val onUndo: (() -> Unit)?,
  val onConfirmChoice: () -> Unit,
  val onChangeSettings: () -> Unit,
  val onChangeShowUnstealableGifts: (Boolean) -> Unit,
  val onChangeAutoScrollSpeed: (Int) -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    val backBehaviour = if (isReadOnly) {
      BackBehaviour.Hidden
    } else if (onUndo != null) {
      BackBehaviour.Enabled(onUndo)
    } else {
      BackBehaviour.Disabled
    }

    val gridState = rememberLazyGridState()
    val autoScrollState = rememberGridAutoScrollState(gridState, autoScrollSpeed = autoScrollSpeed)
    var scrollSpeed by autoScrollState.scrollSpeed

    AppScaffold(
      onBack = backBehaviour,
      title = {
        TwoLineText(
          title = Strings.round.evaluate(roundNumber),
          description = Strings.stealRoundTitle.evaluate(playerName),
        )
      },
      actionIcons = {
        val speedIcon = when (scrollSpeed) {
          0 -> rememberVectorPainter(image = Icons.Default.LooksZero)
          1 -> rememberVectorPainter(image = Icons.Default.LooksOne)
          2 -> rememberVectorPainter(image = Icons.Default.LooksTwo)
          3 -> rememberVectorPainter(image = Icons.Default.LooksThree)
          else -> error("Illegal autoscroll speed: $scrollSpeed")
        }

        IconButton(onClick = {
          scrollSpeed = (scrollSpeed + 1) % 4
          onChangeAutoScrollSpeed(scrollSpeed)
        }) {
          Icon(
            painter = speedIcon,
            contentDescription = Strings.autoScrollSpeedHint.evaluate(),
          )
        }

        val showHideIcon = when (showUnstealableGifts) {
          true -> rememberVectorPainter(image = Icons.Default.Visibility)
          false -> rememberVectorPainter(image = Icons.Default.VisibilityOff)
        }

        IconButton(onClick = { onChangeShowUnstealableGifts(!showUnstealableGifts) }) {
          Icon(
            painter = showHideIcon,
            contentDescription = if (showUnstealableGifts) {
              Strings.hideUnstealableGifts.evaluate()
            } else {
              Strings.showUnstealableGifts.evaluate()
            },
          )
        }

        if (!isReadOnly) {
          IconButton(onClick = { onChangeSettings() }) {
            Icon(
              painter = rememberVectorPainter(image = Icons.Default.Settings),
              contentDescription = Strings.settings.evaluate(),
            )
          }
        }
      }
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(vertical = 16.dp)
      ) {
        GridStateAutoScrollEffect(autoScrollState)

        var lastTouch by remember { mutableStateOf(markNow()) }
        LaunchedEffect(lastTouch) {
          autoScrollState.pause()
          delay(5000L)
          autoScrollState.start()
        }

        LazyVerticalGrid(
          modifier = Modifier
            .weight(1f)
            .pointerInput(Unit) {
              awaitPointerEventScope {
                while (true) {
                  awaitPointerEvent()
                  lastTouch = markNow()
                }
              }
            },
          state = gridState,
          columns = GridCells.Adaptive(minSize = 256.dp),
        ) {
          items(choices, key = { it.key }) { choice ->
            when (choice) {
              is Open -> {
                ChoiceRow(
                  modifier = Modifier.animateItemPlacement(),
                  titleText = if (isLastRound) {
                    Strings.openLastGiftTitle.evaluate()
                  } else {
                    Strings.openNewGiftTitle.evaluate()
                  },
                  descriptionText = if (isLastRound) {
                    Strings.openGiftEndGame.evaluate()
                  } else {
                    Strings.openGiftDescription.evaluate()
                  },
                  readOnly = isReadOnly,
                  isSelected = choice.isSelected,
                  onClick = choice.onPicked,
                )
              }

              is Steal -> {
                if (!choice.isEnabled && !showUnstealableGifts) {
                  // Do not show this row if it can't be stolen
                } else {
                  ChoiceRow(
                    modifier = Modifier.animateItemPlacement(),
                    titleText = Strings.stealFrom.evaluate(choice.playerName),
                    descriptionText = choice.giftName,
                    readOnly = isReadOnly,
                    isSelected = choice.isSelected,
                    onClick = choice.onPicked.takeIf { choice.isEnabled },
                  )
                }
              }
            }
          }
        }

        if (!isReadOnly) {
          FilledTonalButton(
            modifier = Modifier
              .fillMaxWidth()
              .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            enabled = choices.any { it.isSelected },
            onClick = { onConfirmChoice() },
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

@Composable
private fun ChoiceRow(
  modifier: Modifier = Modifier,
  titleText: String,
  descriptionText: String,
  readOnly: Boolean,
  isSelected: Boolean,
  onClick: (() -> Unit)?,
) {
  val clickableModifier = if (onClick != null && !readOnly) {
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
    if (!readOnly) {
      RadioButton(
        modifier = Modifier.size(56.dp),
        onClick = onClick,
        enabled = onClick != null,
        selected = isSelected,
      )
    } else {
      Spacer(
        modifier = Modifier.size(16.dp),
      )
    }

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
