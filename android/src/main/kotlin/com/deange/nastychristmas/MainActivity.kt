package com.deange.nastychristmas

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.deange.nastychristmas.ui.compose.fontsAssetManager
import com.deange.nastychristmas.ui.compose.initTypography
import com.deange.nastychristmas.ui.resources.EN
import com.deange.nastychristmas.ui.resources.StringResources
import com.deange.nastychristmas.ui.theme.Language
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.workflow.WorkflowRendering
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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

    lifecycleScope.launch(Dispatchers.IO) {
      lifecycle.repeatOnLifecycle(CREATED) {
        mainApplication.gameSaver.onGameSaved().collect { game ->
          val desiredOrientation = if (game?.isGameActive == true) {
            SCREEN_ORIENTATION_SENSOR_LANDSCAPE
          } else {
            SCREEN_ORIENTATION_UNSPECIFIED
          }

          withContext(Dispatchers.Main) {
            requestedOrientation = desiredOrientation
          }
        }
      }
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
