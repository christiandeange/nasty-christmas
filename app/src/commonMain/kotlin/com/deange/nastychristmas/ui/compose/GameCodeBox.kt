package com.deange.nastychristmas.ui.compose

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.ui.theme.Strings

@Composable
fun GameCodeBox(gameCode: String) {
  Column(
    modifier = Modifier.padding(horizontal = 8.dp),
    verticalArrangement = spacedBy(4.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      style = MaterialTheme.typography.bodySmall,
      text = Strings.gameCode.evaluate(),
    )
    Text(
      style = MaterialTheme.typography.bodySmall.copy(fontWeight = Bold),
      text = gameCode,
    )
  }
}
