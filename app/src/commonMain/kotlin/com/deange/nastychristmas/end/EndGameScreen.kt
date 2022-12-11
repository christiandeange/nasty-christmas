package com.deange.nastychristmas.end

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Restart
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.ui.compose.AppScaffold
import com.deange.nastychristmas.ui.compose.BackBehaviour.Hidden
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering
import kotlin.math.floor

class EndGameScreen(
  val stats: GameStats,
  val onContinue: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    AppScaffold(
      onBack = Hidden,
      title = { Text(Strings.thanksForPlaying.evaluate()) },
    ) {
      val transition = rememberInfiniteTransition()
      val index by transition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
          animation = tween(16_000, easing = LinearEasing),
          repeatMode = Restart,
        )
      )
      Column(modifier = Modifier.fillMaxSize()) {
        LinearProgressIndicator(
          progress = index / 4f,
          modifier = Modifier.fillMaxWidth().height(2.dp),
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
          Crossfade(
            modifier = Modifier.fillMaxWidth().align(Center),
            targetState = floor(index).toInt(),
          ) { statIndex ->
            when (statIndex % 4) {
              0 -> {
                GameStat(
                  title = Strings.statMostSteals.evaluate(),
                  values = stats.stealsByPlayer,
                  formatter = { it.name },
                )
              }
              1 -> {
                GameStat(
                  title = Strings.statMostStolenFrom.evaluate(),
                  values = stats.stolenFromByPlayer,
                  formatter = { it.name },
                )
              }
              2 -> {
                GameStat(
                  title = Strings.statMostOpens.evaluate(),
                  values = stats.opensByPlayer,
                  formatter = { it.name },
                )
              }
              3 -> {
                GameStat(
                  title = Strings.statMostStolenGift.evaluate(),
                  values = stats.stealsByGift,
                  formatter = { it.name },
                )
              }
            }
          }
        }

        FilledTonalButton(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
          onClick = { onContinue() },
        ) {
          Text(
            style = LocalTextStyle.current.copy(fontWeight = Bold),
            text = Strings.newGame.evaluate().uppercase(),
          )
        }
      }
    }
  }

  @Composable
  private fun <T> GameStat(
    title: String,
    values: Map<T, Int>,
    formatter: (T) -> String,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalAlignment = CenterHorizontally,
      verticalArrangement = spacedBy(8.dp),
    ) {
      Text(
        text = title.uppercase(),
        textAlign = TextAlign.Center,
        style = LocalTextStyle.current.copy(fontWeight = Bold),
      )

      val max = values.values.maxOrNull() ?: 0
      val description = if (max == 0) {
        Strings.notApplicable.evaluate()
      } else {
        values
          .filterValues { it == max }
          .keys
          .joinToString(
            separator = " / ",
            transform = formatter,
          )
      }

      Text(
        text = max.toString(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayLarge.copy(fontWeight = Black),
      )

      Text(
        text = description,
        textAlign = TextAlign.Center,
      )
    }
  }
}
