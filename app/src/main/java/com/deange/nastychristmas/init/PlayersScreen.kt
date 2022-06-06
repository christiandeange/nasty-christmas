package com.deange.nastychristmas.init

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults.filledTonalButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.R
import com.deange.nastychristmas.ui.compose.animatedItemsIndexed
import com.deange.nastychristmas.ui.compose.rememberAnimatedItemsState
import com.deange.nastychristmas.ui.theme.LocalColorTheme
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.asMutableState

@OptIn(ExperimentalFoundationApi::class, WorkflowUiExperimentalApi::class)
class PlayersScreen(
  private val players: List<String>,
  private val currentPlayer: TextController,
  private val onAddPlayer: (String) -> Unit,
  private val onDeletePlayer: (Int) -> Unit,
  private val onBack: () -> Unit,
  private val onStartGame: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    BackHandler(onBack = onBack)

    Column {
      NastyChristmasTheme(darkTheme = LocalColorTheme.current.isLight()) {
        Surface(color = MaterialTheme.colorScheme.inversePrimary) {
          Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = spacedBy(16.dp),
          ) {
            var currentPlayerName by currentPlayer.asMutableState()

            OutlinedTextField(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(20.dp),
              value = currentPlayerName,
              onValueChange = { currentPlayerName = it.replace("\n", "") },
              label = { Text(stringResource(R.string.player_hint)) },
              singleLine = true,
              keyboardOptions = KeyboardOptions(imeAction = Send),
              keyboardActions = KeyboardActions(onSend = { onAddPlayer(currentPlayerName) }),
            )

            FilledTonalButton(
              modifier = Modifier
                .size(56.dp)
                .align(Bottom),
              enabled = currentPlayerName.isNotBlank() && currentPlayerName !in players,
              onClick = { onAddPlayer(currentPlayerName) },
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

      val listState = rememberLazyListState()
      val animationState by rememberAnimatedItemsState(players)

      if (animationState.isNotEmpty()) {
        LaunchedEffect(animationState.size, animationState.last().visibility.isIdle) {
          if (animationState.last().visibility.isIdle) {
            listState.animateScrollToItem(animationState.size)
          }
        }
      }

      LazyColumn(modifier = Modifier.weight(1f), state = listState) {
        animatedItemsIndexed(animationState, key = { name -> name }) { i, name ->
          Column(modifier = Modifier.animateItemPlacement()) {
            Text(
              modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                  onClick = { },
                  onLongClick = { onDeletePlayer(i) },
                )
                .padding(16.dp),
              text = name,
            )

            Divider(
              color = MaterialTheme.colorScheme.outline,
              thickness = Hairline,
            )
          }
        }
      }

      FilledTonalButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        enabled = players.size >= 2,
        onClick = { onStartGame() },
        colors = filledTonalButtonColors(),
      ) {
        Text(
          style = LocalTextStyle.current.copy(fontWeight = Bold),
          text = stringResource(R.string.lets_get_nasty).uppercase(),
        )
      }
    }
  }
}
