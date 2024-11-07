package com.deange.nastychristmas.init

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deange.nastychristmas.ui.compose.BackHandler
import com.deange.nastychristmas.ui.compose.SimpleList
import com.deange.nastychristmas.ui.compose.SimpleListItem.OneLine
import com.deange.nastychristmas.ui.compose.SimpleText
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.compose.asMutableState
import com.deange.nastychristmas.ui.compose.flow.FlowRow
import com.deange.nastychristmas.ui.compose.flow.SizeMode
import com.deange.nastychristmas.ui.compose.rememberLoginGradientBrush
import com.deange.nastychristmas.ui.compose.serifFont
import com.deange.nastychristmas.ui.theme.AppTypography
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering

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

    Surface(modifier = Modifier.fillMaxSize().background(rememberLoginGradientBrush())) {
      Column(
        modifier = Modifier.background(rememberLoginGradientBrush()).padding(horizontal = 16.dp),
        horizontalAlignment = CenterHorizontally,
      ) {
        var currentPlayerName by currentPlayer.asMutableState()
        val isEnabled by derivedStateOf {
          currentPlayerName.isNotBlank() && currentPlayerName !in players
        }

        MaterialTheme(typography = AppTypography(serifFont)) {
          Text(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            text = Strings.appName.evaluate(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge.copy(
              lineHeight = MaterialTheme.typography.displayLarge.lineHeight * 0.75f,
              letterSpacing = 3.sp,
              fontWeight = Bold,
              color = MaterialTheme.colorScheme.onPrimary
            ),
          )
        }

        val darkColor = Color.Black.copy(alpha = 0.80f)
        OutlinedTextField(
          modifier = Modifier.padding(horizontal = 72.dp, vertical = 8.dp),
          shape = CircleShape,
          value = currentPlayerName,
          onValueChange = { currentPlayerName = it },
          placeholder = { Text(Strings.playerHint.evaluate()) },
          singleLine = true,
          supportingText = {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = Strings.nastyGiftPromo.evaluate(),
              textAlign = TextAlign.Center,
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = Bold),
            )
          },
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = darkColor,
            focusedContainerColor = darkColor,
            unfocusedBorderColor = darkColor,
            unfocusedSupportingTextColor = darkColor,
            focusedBorderColor = darkColor,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedSupportingTextColor = darkColor,
          ),
          keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrectEnabled = false,
            imeAction = Send,
          ),
          keyboardActions = KeyboardActions(onSend = {
            if (isEnabled) {
              onAddPlayer(currentPlayerName)
            }
          }),
          trailingIcon = {
            IconButton(onClick = {
              if (isEnabled) {
                onAddPlayer(currentPlayerName)
              }
            }) {
              Icon(
                painter = rememberVectorPainter(Icons.Default.Add),
                contentDescription = Strings.add.evaluate(),
              )
            }
          }
        )

        Spacer(modifier = Modifier.weight(1f))

        val scrollState = rememberScrollState()
        LaunchedEffect(players.size) {
          scrollState.animateScrollTo(scrollState.maxValue)
        }

        FlowRow(
          modifier = Modifier.verticalScroll(scrollState),
          mainAxisSize = SizeMode.Expand,
          mainAxisSpacing = 8.dp,
          crossAxisSpacing = 0.dp,
        ) {
          players.forEachIndexed { i, player ->
            InputChip(
              label = { Text(player) },
              shape = CircleShape,
              colors = InputChipDefaults.inputChipColors(
                containerColor = darkColor,
              ),
              trailingIcon = {
                Icon(
                  painter = rememberVectorPainter(Icons.Default.Clear),
                  contentDescription = Strings.removePlayer.evaluate(player),
                )
              },
              onClick = { onDeletePlayer(i) },
              selected = false,
            )
          }
        }
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Center,
        ) {
          ElevatedButton(
            modifier = Modifier.padding(vertical = 8.dp),
            enabled = players.size >= 2 && currentPlayerName.isBlank(),
            onClick = { onStartGame() },
          ) {
            Text(
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = Bold),
              text = Strings.letsGetNasty.evaluate().uppercase(),
            )
          }
        }
      }
    }
  }
}

class ReadOnlyPlayersScreen(
  private val players: List<String>,
) : ViewRendering {
  @Composable
  override fun Content() {
    BackHandler(onBack = { /* no-op */ })

    SimpleList(
      title = SimpleText(Strings.players),
      items = players.map { player -> OneLine(SimpleText(player)) },
    )
  }
}
