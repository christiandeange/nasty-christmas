package com.deange.nastychristmas.end

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.core.MR
import com.deange.nastychristmas.ui.compose.BackHandler
import com.deange.nastychristmas.ui.compose.evaluate
import com.deange.nastychristmas.ui.workflow.ViewRendering

class EndGameScreen(
  val onContinue: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    BackHandler(onBack = onContinue)

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      Text(
        modifier = Modifier.align(Alignment.TopStart),
        text = MR.strings.thanks_for_playing.evaluate(),
        style = MaterialTheme.typography.titleLarge,
      )

      FilledTonalButton(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter),
        onClick = { onContinue() },
      ) {
        Text(
          style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
          text = MR.strings.ok.evaluate(),
        )
      }
    }
  }
}