package com.deange.nastychristmas.round

import android.view.animation.AccelerateInterpolator
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deange.nastychristmas.R
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.ui.workflow.ViewRendering
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class NewRoundPlayerSelectionScreen(
  val playerPool: Set<Player>,
  val round: Int,
  val onPlayerSelected: (Player) -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    val coroutineScope = rememberCoroutineScope()
    val finalTimeout = remember { Random.nextLong(1000L, 2000L) }
    val interpolator = remember { AccelerateInterpolator(6f) }

    val frameDelays = LongArray(50)
    for (i in frameDelays.indices) {
      val t = i / (frameDelays.size - 1).toFloat()
      frameDelays[i] = lerp(11f, finalTimeout.toFloat(), interpolator.getInterpolation(t)).toLong()
    }

    var frame: Int by remember { mutableStateOf(0) }
    var randomPlayer by remember { mutableStateOf(playerPool.random()) }

    if (frame <= frameDelays.lastIndex) {
      LaunchedEffect(frame) {
        coroutineScope.launch {
          delay(frameDelays[frame])
          frame += 1
          randomPlayer = playerPool.random()
        }
      }
    } else {
      onPlayerSelected(randomPlayer)
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
      text = stringResource(R.string.round_title, round),
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
          text = stringResource(android.R.string.ok).uppercase(),
        )
      }
    }
  }
}

private fun lerp(start: Float, stop: Float, fraction: Float) =
  (start * (1 - fraction) + stop * fraction)
