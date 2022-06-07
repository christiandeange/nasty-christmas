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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.R
import com.deange.nastychristmas.ui.workflow.ViewRendering

class EndGameScreen(
  val onContinue: () -> Unit,
) : ViewRendering {
  @Composable
  override fun Content() {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      Text(
        modifier = Modifier.align(Alignment.TopStart),
        text = stringResource(R.string.thanks_for_playing),
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
          text = stringResource(android.R.string.ok).uppercase(),
        )
      }
    }
  }
}