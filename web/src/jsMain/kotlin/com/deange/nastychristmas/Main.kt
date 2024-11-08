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

private val storage: PersistentStorage by lazy {
  BrowserLocalStorage(window.localStorage, keyPrefix = "hohoho")
}

private val firebase: Firebase by lazy {
  initializeFirebase(configuration)
}

private val random by lazy {
  Random(seed = Date.now().toLong())
}

private val gameSaver by lazy {
  GameSaver(storage, firebase.firestore, CoroutineScope(Dispatchers.Default) + SupervisorJob())
}

private val appWorkflow by lazy {
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

private val registryWorkflow by lazy {
  RegistryWorkflow(
    appWorkflow = appWorkflow,
    firestore = firebase.firestore,
  )
}

@OptIn(ExperimentalComposeUiApi::class)
private fun runMainApp() {
  var game: GameState? by gameSaver.game()

  val initialProps: AppProps = when (val restoredGameState = gameSaver.restore()) {
    null -> NewGame
    else -> RestoredFromSave(restoredGameState, isReadOnly = false)
  }

  CanvasBasedWindow(title = "Nasty Christmas") {
    App(
      workflow = appWorkflow,
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

@OptIn(ExperimentalComposeUiApi::class)
fun runRegistryApp() {
  CanvasBasedWindow(title = "Nasty Christmas Registry") {
    App(
      workflow = registryWorkflow,
      props = Unit,
      onOutput = { },
    )
  }
}
