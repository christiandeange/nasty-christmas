package com.deange.nastychristmas.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.deange.nastychristmas.ui.compose.BackBehaviour.Disabled
import com.deange.nastychristmas.ui.compose.BackBehaviour.Enabled
import com.deange.nastychristmas.ui.compose.BackBehaviour.Hidden
import com.deange.nastychristmas.ui.theme.LocalColorTheme
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.theme.Strings

@Composable
fun AppScaffold(
  onBack: BackBehaviour,
  title: @Composable () -> Unit,
  actionIcons: @Composable RowScope.() -> Unit = {},
  content: @Composable () -> Unit,
) {
  BackHandler(onBack = {
    when (onBack) {
      is Hidden,
      is Disabled -> Unit
      is Enabled -> onBack.onBack()
    }
  })

  Scaffold(
    topBar = {
      NastyChristmasTheme(darkTheme = LocalColorTheme.current.isLight()) {
        SmallTopAppBar(
          colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
          ),
          navigationIcon = {
            val icon: @Composable () -> Unit = {
              Icon(
                painter = rememberVectorPainter(Icons.Default.ArrowBack),
                contentDescription = Strings.back.evaluate(),
              )
            }

            when (onBack) {
              is Hidden -> Unit
              is Disabled -> {
                IconButton(
                  onClick = {},
                  enabled = false,
                  content = icon,
                )
              }
              is Enabled -> {
                IconButton(
                  onClick = onBack.onBack,
                  content = icon,
                )
              }
            }
          },
          title = title,
          actions = actionIcons,
        )
      }
    },
  ) { contentPadding ->
    Box(modifier = Modifier.padding(contentPadding).fillMaxSize()) {
      content()
    }
  }
}

sealed interface BackBehaviour {
  object Hidden : BackBehaviour
  object Disabled : BackBehaviour
  data class Enabled(val onBack: () -> Unit) : BackBehaviour
}
