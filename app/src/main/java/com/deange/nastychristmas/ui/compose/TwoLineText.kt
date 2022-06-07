package com.deange.nastychristmas.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TwoLineText(
  modifier: Modifier = Modifier,
  title: String,
  description: String,
  textColor: Color = Color.Unspecified,
) {
  Column(
    modifier = modifier.padding(vertical = 4.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Bold,
        color = textColor,
      ),
    )

    Text(
      text = description,
      style = MaterialTheme.typography.bodySmall.copy(
        color = textColor,
      ),
    )
  }
}
