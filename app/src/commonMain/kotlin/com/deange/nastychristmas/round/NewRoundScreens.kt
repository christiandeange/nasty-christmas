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
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.ui.compose.BackHandler
import com.deange.nastychristmas.ui.compose.konfetti.render.KonfettiView
import com.deange.nastychristmas.ui.compose.konfetti.render.PresetKonfetti
import com.deange.nastychristmas.ui.theme.Strings
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
    var playerShown by remember { mutableStateOf(playerPool.random(random)) }

    fun randomPlayer(): Player {
      // Selects a player that isn't currently selected.
      return (playerPool - playerShown).random(random)
    }

    LaunchedEffect(frame) {
      if (frame < frameDelays.lastIndex) {
        delay(frameDelays[frame])
        frame++
        playerShown = randomPlayer()
      } else {
        delay(frameDelays[frame])
        if (random.nextBoolean()) {
          // 50% of the time, a new random player is selected right before the selection ends.
          playerShown = randomPlayer()
        }
        onPlayerSelected(playerShown)
      }
    }

    PlayerSelectionScreen(
      player = playerShown,
      round = round,
      onContinue = null
    )
  }
}

class NewRoundPlayerScreen(
  val random: Random,
  val player: Player,
  val round: Int,
  val onContinue: () -> Unit
) : ViewRendering {
  @Composable
  override fun Content() {
    BackHandler(onBack = onContinue)
    Box {
      KonfettiView(
        modifier = Modifier.fillMaxSize(),
        parties = PresetKonfetti.explode(),
        random = random,
      )

      PlayerSelectionScreen(
        player = player,
        round = round,
        onContinue = onContinue,
      )
    }
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
      text = Strings.roundTitle.evaluate(round),
      style = MaterialTheme.typography.titleLarge,
    )

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Center),
      text = player.name.uppercase(),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = Bold,
        letterSpacing = 3.sp,
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
          text = Strings.ok.evaluate(),
        )
      }
    }
  }
}

private fun lerp(start: Float, stop: Float, fraction: Float) =
  (start * (1 - fraction) + stop * fraction)
