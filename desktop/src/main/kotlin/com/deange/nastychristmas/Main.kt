package com.deange.nastychristmas

import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.firebase.Firebase
import com.deange.nastychristmas.firebase.initializeFirebase
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.state.GameSaver
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.store.DataStoreStorage
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.ui.compose.initTypography
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.workflow.App
import com.deange.nastychristmas.workflow.AppOutput
import com.deange.nastychristmas.workflow.AppProps
import com.deange.nastychristmas.workflow.AppProps.NewGame
import com.deange.nastychristmas.workflow.AppProps.RestoredFromSave
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

  val firebase: Firebase by lazy {
    initializeFirebase()
  }

  val random by lazy {
    Random(seed = System.currentTimeMillis())
  }

  val workflow by lazy {
    AppWorkflow(
      playersWorkflow = PlayersWorkflow(),
      newRoundWorkflow = NewRoundWorkflow(),
      openGiftWorkflow = OpenGiftWorkflow(),
      stealingRoundWorkflow = StealingRoundWorkflow(storage),
      endGameWorkflow = EndGameWorkflow(),
      gameSettingsWorkflow = GameSettingsWorkflow(),
      random = random,
    )
  }

  val gameSaver = GameSaver(storage, firebase.firestore, CoroutineScope(IO) + SupervisorJob())
  var game: GameState? by gameSaver.game()

  val initialProps: AppProps = when (val restoredGameState = gameSaver.restore()) {
    null -> NewGame
    else -> RestoredFromSave(restoredGameState, isReadOnly = false)
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
      title = Strings.appName.evaluate(),
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
