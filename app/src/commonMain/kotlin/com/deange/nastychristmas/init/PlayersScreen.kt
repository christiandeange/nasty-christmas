package com.deange.nastychristmas.init

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults.filledTonalButtonElevation
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.core.MR
import com.deange.nastychristmas.ui.compose.BackHandler
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.compose.asMutableState
import com.deange.nastychristmas.ui.compose.evaluate
import com.deange.nastychristmas.ui.theme.LocalColorTheme
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.workflow.ViewRendering

@OptIn(ExperimentalFoundationApi::class)
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
      var currentPlayerName by currentPlayer.asMutableState()

      NastyChristmasTheme(darkTheme = LocalColorTheme.current.isLight()) {
        Surface(color = MaterialTheme.colorScheme.inversePrimary) {
          Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = spacedBy(16.dp),
          ) {
            val isEnabled = currentPlayerName.isNotBlank() && currentPlayerName !in players

            OutlinedTextField(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(20.dp),
              value = currentPlayerName,
              onValueChange = { currentPlayerName = it },
              label = { Text(MR.strings.player_hint.evaluate()) },
              singleLine = true,
              keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = Send,
              ),
              keyboardActions = KeyboardActions(onSend = {
                if (isEnabled) {
                  onAddPlayer(currentPlayerName)
                }
              }),
            )

            FilledTonalButton(
              modifier = Modifier
                .size(56.dp)
                .align(Bottom),
              enabled = isEnabled,
              onClick = { onAddPlayer(currentPlayerName) },
              contentPadding = PaddingValues(0.dp),
              elevation = filledTonalButtonElevation(
                defaultElevation = 8.dp,
                focusedElevation = 8.dp,
                pressedElevation = 16.dp,
              )
            ) {
              Icon(
                painter = rememberVectorPainter(Icons.Default.Add),
                contentDescription = MR.strings.add.evaluate(),
              )
            }
          }
        }
      }

      val listState = rememberLazyListState()
      LaunchedEffect(players.size) {
        listState.animateScrollToItem(players.size)
      }

      LazyColumn(modifier = Modifier.weight(1f), state = listState) {
        itemsIndexed(players, key = { _, name -> name }) { i, name ->
          Column {
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
        enabled = players.size >= 2 && currentPlayerName.isBlank(),
        onClick = { onStartGame() },
      ) {
        Text(
          style = LocalTextStyle.current.copy(fontWeight = Bold),
          text = MR.strings.lets_get_nasty.evaluate().uppercase(),
        )
      }
    }
  }
}
