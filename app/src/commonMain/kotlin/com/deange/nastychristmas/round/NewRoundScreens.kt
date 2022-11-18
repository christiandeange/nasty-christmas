package com.deange.nastychristmas.round

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deange.nastychristmas.core.MR
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.ui.compose.BackHandler
import com.deange.nastychristmas.ui.compose.evaluate
import com.deange.nastychristmas.ui.workflow.ViewRendering
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.random.Random

class NewRoundPlayerSelectionScreen(
  val random: Random,
  val playerPool: Set<Player>,
  val round: Int,
  val onPlayerSelected: (Player) -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    BackHandler(onBack = { /* no-op */ })

    val frameDelays = remember {
      // Last delay is between 1.25s and 1.75s, everything prior exponentially decays to that amount.
      val finalTimeout = random.nextLong(1250L, 1750L)
      val interpolator = Easing { it.pow(12f) }

      LongArray(50).apply {
        for (i in indices) {
          val t = i / (size - 1).toFloat()
          this[i] = lerp(1f, finalTimeout.toFloat(), interpolator.transform(t)).toLong()
        }
      }
    }

    var frame: Int by remember { mutableStateOf(0) }
    var randomPlayer by remember { mutableStateOf(playerPool.random(random)) }

    LaunchedEffect(frame) {
      if (frame < frameDelays.lastIndex) {
        delay(frameDelays[frame])
        frame++
        randomPlayer = (playerPool - randomPlayer).random(random)
      } else {
        delay(frameDelays[frame])
        onPlayerSelected(randomPlayer)
      }
    }

    PlayerSelectionScreen(randomPlayer, round, onContinue = null)
  }
}

class NewRoundPlayerScreen(
  val player: Player,
  val round: Int,
  val onContinue: () -> Unit
) : ViewRendering {
  @Composable
  override fun Content() {
    BackHandler(onBack = onContinue)
    PlayerSelectionScreen(player, round, onContinue)
  }
}

@Composable
private fun PlayerSelectionScreen(
  player: Player,
  round: Int,
  onContinue: (() -> Unit)?,
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Text(
      modifier = Modifier.align(Alignment.TopStart),
      text = MR.strings.round_title.evaluate(round),
      style = MaterialTheme.typography.titleLarge,
    )

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Center),
      text = player.name,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.headlineLarge.copy(
        letterSpacing = 3.sp,
        fontFeatureSettings = "smcp",
      ),
    )

    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = onContinue != null

    AnimatedVisibility(
      modifier = Modifier.align(Alignment.BottomCenter),
      visibleState = visibleState,
      enter = fadeIn(),
      exit = fadeOut(),
    ) {
      FilledTonalButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onContinue!!() },
      ) {
        Text(
          style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
          text = MR.strings.ok.evaluate(),
        )
      }
    }
  }
}

private fun lerp(start: Float, stop: Float, fraction: Float) =
  (start * (1 - fraction) + stop * fraction)