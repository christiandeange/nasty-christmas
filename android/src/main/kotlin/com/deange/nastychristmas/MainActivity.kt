package com.deange.nastychristmas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import com.deange.nastychristmas.ui.compose.fontsAssetManager
import com.deange.nastychristmas.ui.compose.initTypography
import com.deange.nastychristmas.ui.resources.EN
import com.deange.nastychristmas.ui.resources.StringResources
import com.deange.nastychristmas.ui.theme.Language
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.workflow.WorkflowRendering
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
  private val mainApplication by lazy {
    application as MainApplication
  }

  private val viewModel: AppViewModel by viewModels {
    AppViewModelFactory(
      owner = this,
      gameSaver = mainApplication.gameSaver,
      appWorkflow = mainApplication.workflow,
      defaultArgs = intent.extras,
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()

    super.onCreate(savedInstanceState)

    runBlocking {
      fontsAssetManager = assets
      initTypography()
    }

    with(WindowCompat.getInsetsController(window, window.decorView)) {
      systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
      hide(systemBars())
    }

    setContent {
      NastyChristmasTheme {
        Language(StringResources.EN) {
          WorkflowRendering(viewModel.renderings) { screen ->
            Surface(Modifier.fillMaxSize()) {
              Box {
                screen.Content()
              }
            }
          }
        }
      }

      LaunchedEffect(viewModel) {
        viewModel.waitForExit()
        finish()
      }
    }
  }
}
