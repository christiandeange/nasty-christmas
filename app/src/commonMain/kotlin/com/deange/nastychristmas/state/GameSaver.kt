package com.deange.nastychristmas.state

import com.deange.nastychristmas.firebase.Firestore
import com.deange.nastychristmas.firebase.add
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.store.preference
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

  private suspend fun saveGameState(gameState: GameState?) {
    if (gameState != null) {
      firestore.add("game-state/default/", gameState)
    } else {
      firestore.delete("game-state/default/")
    }
  }
}
