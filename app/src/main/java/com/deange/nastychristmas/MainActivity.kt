package com.deange.nastychristmas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.storage.GameStateStorage
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.theme.StatusBarTheme
import com.deange.nastychristmas.ui.workflow.WorkflowRenderings
import com.deange.nastychristmas.workflow.AppViewModel
import com.deange.nastychristmas.workflow.AppViewModelFactory
import com.deange.nastychristmas.workflow.AppWorkflow
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import kotlin.random.Random

@OptIn(WorkflowUiExperimentalApi::class)
class MainActivity : ComponentActivity() {

  private val storage by lazy {
    GameStateStorage(application)
  }

  private val random by lazy {
    Random(seed = System.currentTimeMillis())
  }

  private val workflow by lazy {
    AppWorkflow(
      playersWorkflow = PlayersWorkflow(),
      newRoundWorkflow = NewRoundWorkflow(random),
      openGiftWorkflow = OpenGiftWorkflow(),
      stealingRoundWorkflow = StealingRoundWorkflow(),
      endGameWorkflow = EndGameWorkflow(),
      gameSettingsWorkflow = GameSettingsWorkflow(),
    )
  }

  private val viewModel: AppViewModel by viewModels {
    AppViewModelFactory(this, storage, workflow, intent.extras)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      NastyChristmasTheme {
        StatusBarTheme()
        WorkflowRenderings(viewModel.renderings) { screen ->
          Surface(Modifier.fillMaxSize()) {
            Box {
              screen.Content()
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
