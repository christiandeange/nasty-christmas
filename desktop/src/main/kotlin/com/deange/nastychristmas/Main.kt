package com.deange.nastychristmas

import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.deange.nastychristmas.core.MR
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.store.DataStoreStorage
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.store.preference
import com.deange.nastychristmas.ui.compose.evaluate
import com.deange.nastychristmas.ui.compose.initTypography
import com.deange.nastychristmas.workflow.App
import com.deange.nastychristmas.workflow.AppOutput
import com.deange.nastychristmas.workflow.AppProps
import com.deange.nastychristmas.workflow.AppWorkflow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.random.Random

fun main() {
  val storage: PersistentStorage by lazy {
    val folder = File(System.getProperty("user.home")).resolve(".christmas").also { it.mkdirs() }
    DataStoreStorage(
      PreferenceDataStoreFactory.create(
        scope = CoroutineScope(IO) + SupervisorJob(),
        produceFile = { folder.resolve("settings.preferences_pb") }
      )
    )
  }

  val random by lazy {
    Random(seed = System.currentTimeMillis())
  }

  val workflow by lazy {
    AppWorkflow(
      playersWorkflow = PlayersWorkflow(),
      newRoundWorkflow = NewRoundWorkflow(random),
      openGiftWorkflow = OpenGiftWorkflow(),
      stealingRoundWorkflow = StealingRoundWorkflow(),
      endGameWorkflow = EndGameWorkflow(),
      gameSettingsWorkflow = GameSettingsWorkflow(),
    )
  }

  var game by storage.preference<GameState?>("game-state", null)
  val initialProps: AppProps = if (game == null) {
    AppProps.NewGame
  } else {
    AppProps.RestoredFromSave(game!!)
  }

  runBlocking {
    initTypography()
  }

  application {
    val windowState = rememberWindowState(
      size = DpSize(800.dp, 450.dp),
      position = WindowPosition(Center),
    )

    Window(
      onCloseRequest = ::exitApplication,
      title = MR.strings.app_name.evaluate(),
      state = windowState,
    ) {
      App(
        workflow = workflow,
        props = initialProps,
        onOutput = { output ->
          when (output) {
            is AppOutput.Exit -> exitApplication()
            is AppOutput.ClearGameState -> game = null
            is AppOutput.SaveGameState -> game = output.gameState
          }
        },
      )
    }
  }
}
