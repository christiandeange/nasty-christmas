package com.deange.nastychristmas.init

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import christmasBackground
import com.deange.nastychristmas.ui.compose.BackHandler
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.compose.asMutableState
import com.deange.nastychristmas.ui.compose.flow.FlowRow
import com.deange.nastychristmas.ui.compose.flow.SizeMode
import com.deange.nastychristmas.ui.compose.glitchEffect
import com.deange.nastychristmas.ui.compose.serifFont
import com.deange.nastychristmas.ui.theme.AppTypography
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.seconds

class PlayersScreen(
  private val random: Random,
  private val players: List<String>,
  private val currentPlayer: TextController,
  private val gameCode: String?,
  private val isReadOnly: Boolean,
  private val onAddPlayer: (String) -> Unit,
  private val onDeletePlayer: (Int) -> Unit,
  private val onBack: () -> Unit,
  private val onStartGame: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    BackHandler(onBack = onBack)

    Box(modifier = Modifier.fillMaxSize().christmasBackground()) {
      Column(
        modifier = Modifier.padding(horizontal = 16.dp).imePadding().verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally,
      ) {
        var currentPlayerName by currentPlayer.asMutableState()
        val isEnabled by derivedStateOf {
          currentPlayerName.isNotBlank() && currentPlayerName !in players
        }

        var key by remember { mutableStateOf(0) }
        LaunchedEffect(key) {
          delay(random.nextInt(4..8).seconds)
          key++
        }

        MaterialTheme(typography = AppTypography(serifFont)) {
          Text(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp).glitchEffect(key),
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
          placeholder = {
            Text(
              if (!isReadOnly) {
                Strings.playerHint.evaluate()
              } else {
                Strings.addingPlayers.evaluate()
              }
            )
          },
          singleLine = true,
          readOnly = isReadOnly,
          supportingText = {
            if (!isReadOnly) {
              Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (gameCode == null) {
                  Strings.nastyGiftPromo.evaluate()
                } else {
                  Strings.nastyGiftPromoWithGameCode.evaluate(gameCode)
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = Bold),
              )
            }
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
            if (!isReadOnly) {
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
          }
        )

        if (!isReadOnly) Spacer(modifier = Modifier.weight(1f))

        val scrollState = rememberScrollState()
        LaunchedEffect(players.size) {
          scrollState.animateScrollTo(scrollState.maxValue)
        }

        FlowRow(
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
                if (!isReadOnly) {
                  Icon(
                    painter = rememberVectorPainter(Icons.Default.Clear),
                    contentDescription = Strings.removePlayer.evaluate(player),
                  )
                }
              },
              onClick = {
                if (!isReadOnly) {
                  onDeletePlayer(i)
                }
              },
              selected = false,
            )
          }
        }

        if (!isReadOnly) {
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
}
