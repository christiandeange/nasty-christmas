package com.deange.nastychristmas

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.store.BrowserLocalStorage
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.store.preference
import com.deange.nastychristmas.ui.compose.initTypography
import com.deange.nastychristmas.workflow.App
import com.deange.nastychristmas.workflow.AppOutput.ClearGameState
import com.deange.nastychristmas.workflow.AppOutput.Exit
import com.deange.nastychristmas.workflow.AppOutput.SaveGameState
import com.deange.nastychristmas.workflow.AppProps
import com.deange.nastychristmas.workflow.AppProps.NewGame
import com.deange.nastychristmas.workflow.AppWorkflow
import kotlinx.browser.window
import org.jetbrains.skiko.wasm.onWasmReady
import kotlin.js.Date
import kotlin.random.Random

suspend fun main() {
  initTypography()

  onWasmReady {
    runApp()
  }
}

private fun runApp() {
  val storage: PersistentStorage by lazy {
    BrowserLocalStorage(window.localStorage, keyPrefix = "hohoho")
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

  var game by storage.preference<GameState?>("game-state", null)
  val initialProps: AppProps = if (game == null) {
    NewGame
  } else {
    AppProps.RestoredFromSave(game!!)
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
