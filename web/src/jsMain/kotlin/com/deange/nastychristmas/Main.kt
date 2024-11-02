package com.deange.nastychristmas

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.firebase.BuildKonfig.FIREBASE_API_KEY
import com.deange.nastychristmas.firebase.BuildKonfig.FIREBASE_APP_ID
import com.deange.nastychristmas.firebase.BuildKonfig.FIREBASE_AUTH_DOMAIN
import com.deange.nastychristmas.firebase.BuildKonfig.FIREBASE_PROJECT_ID
import com.deange.nastychristmas.firebase.Firebase
import com.deange.nastychristmas.firebase.FirebaseConfiguration
import com.deange.nastychristmas.firebase.initializeFirebase
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.registry.RegistryWorkflow
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.state.GameSaver
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.store.BrowserLocalStorage
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.ui.compose.initTypography
import com.deange.nastychristmas.workflow.App
import com.deange.nastychristmas.workflow.AppOutput.ClearGameState
import com.deange.nastychristmas.workflow.AppOutput.Exit
import com.deange.nastychristmas.workflow.AppOutput.SaveGameState
import com.deange.nastychristmas.workflow.AppProps
import com.deange.nastychristmas.workflow.AppProps.NewGame
import com.deange.nastychristmas.workflow.AppProps.RestoredFromSave
import com.deange.nastychristmas.workflow.AppWorkflow
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import org.jetbrains.skiko.wasm.onWasmReady
import kotlin.js.Date
import kotlin.random.Random

suspend fun main() {
  initTypography()

  onWasmReady {
    runRegistryApp()
//    runMainApp()
  }
}

private val configuration = FirebaseConfiguration(
  applicationId = FIREBASE_APP_ID,
  apiKey = FIREBASE_API_KEY,
  projectId = FIREBASE_PROJECT_ID,
  authDomain = FIREBASE_AUTH_DOMAIN,
)

private fun runMainApp() {
  val storage: PersistentStorage by lazy {
    BrowserLocalStorage(window.localStorage, keyPrefix = "hohoho")
  }

  val firebase: Firebase by lazy {
    initializeFirebase(configuration)
  }

  val random by lazy {
    Random(seed = Date.now().toLong())
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

  val gameSaver = GameSaver(storage, firebase.firestore, CoroutineScope(Dispatchers.Default) + SupervisorJob())
  var game: GameState? by gameSaver.game()

  val initialProps: AppProps = when (val restoredGameState = gameSaver.restore()) {
    null -> NewGame
    else -> RestoredFromSave(restoredGameState)
  }

  @OptIn(ExperimentalComposeUiApi::class)
  CanvasBasedWindow(title = "Nasty Christmas") {
    App(
      workflow = workflow,
      props = initialProps,
      onOutput = { output ->
        when (output) {
          is Exit -> window.close()
          is ClearGameState -> game = null
          is SaveGameState -> game = output.gameState
        }
      },
    )
  }
}

fun runRegistryApp() {
  val storage: PersistentStorage by lazy {
    BrowserLocalStorage(window.localStorage, keyPrefix = "hohoho")
  }

  val firebase: Firebase by lazy {
    initializeFirebase(configuration)
  }

  val workflow by lazy {
    RegistryWorkflow(
      storage = storage,
      firestore = firebase.firestore,
    )
  }

  @OptIn(ExperimentalComposeUiApi::class)
  CanvasBasedWindow(title = "Nasty Christmas Registry") {
    App(
      workflow = workflow,
      props = Unit,
      onOutput = { },
    )
  }
}
