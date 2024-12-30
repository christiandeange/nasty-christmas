package com.deange.nastychristmas.state

import com.deange.nastychristmas.firebase.Firestore
import com.deange.nastychristmas.firebase.add
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.store.preference
import com.deange.nastychristmas.workflow.AppState.ChangeGameSettings
import com.deange.nastychristmas.workflow.AppState.EndGame
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.deange.nastychristmas.workflow.AppState.OpeningGift
import com.deange.nastychristmas.workflow.AppState.PickingPlayer
import com.deange.nastychristmas.workflow.AppState.StealingRound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class GameSaver(
  storage: PersistentStorage,
  private val firestore: Firestore,
  private val updaterScope: CoroutineScope,
) {
  private var game: GameState? by storage.preference("game-state", null)

  fun game(): ReadWriteProperty<Any?, GameState?> {
    return object : ReadWriteProperty<Any?, GameState?> {
      override fun getValue(thisRef: Any?, property: KProperty<*>): GameState? {
        return game
      }

      override fun setValue(thisRef: Any?, property: KProperty<*>, value: GameState?) {
        game = value

        updaterScope.launch {
          saveGameState(value)
        }
      }
    }
  }

  fun restore(): GameState? {
    return runCatching { game }
      .onFailure { e ->
        e.printStackTrace()
        game = null
      }
      .getOrNull()
  }

  suspend fun generateGameCode(): String {
    lateinit var code: String
    do {
      code = List(6) { GAME_CODE_CHARS.random() }.joinToString("")
    } while (firestore.exists("game-state/$code"))

    return code
  }

  private suspend fun saveGameState(gameState: GameState?) {
    val gameCode = when (val appState = gameState?.appState) {
      is InitializingPlayers -> appState.gameCode
      is PickingPlayer -> appState.settings.gameCode
      is StealingRound -> appState.settings.gameCode
      is OpeningGift -> appState.settings.gameCode
      is EndGame -> appState.settings.gameCode
      is ChangeGameSettings -> appState.settings.gameCode
      null -> null
    }

    if (gameState != null && gameCode != null) {
      firestore.add("game-state/$gameCode/", gameState)
    }
  }

  private companion object {
    private val GAME_CODE_CHARS = ('A'..'Z') + ('0'..'9')
  }
}
