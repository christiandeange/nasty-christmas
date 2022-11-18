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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.store.DataStoreStorage
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.ui.compose.appFont
import com.deange.nastychristmas.ui.compose.initTypography
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.theme.StatusBarTheme
import com.deange.nastychristmas.ui.workflow.WorkflowRendering
import com.deange.nastychristmas.workflow.AppWorkflow
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class MainActivity : ComponentActivity() {
  private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

  private val storage: PersistentStorage by lazy {
    DataStoreStorage(dataStore)
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

    runBlocking {
      initTypography()
    }

    setContent {
      NastyChristmasTheme {
        StatusBarTheme()
        WorkflowRendering(viewModel.renderings) { screen ->
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
