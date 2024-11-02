package com.deange.nastychristmas.state

import com.deange.nastychristmas.firebase.CurrentStateSnapshot
import com.deange.nastychristmas.firebase.Firestore
import com.deange.nastychristmas.firebase.GiftSnapshot
import com.deange.nastychristmas.firebase.batch
import com.deange.nastychristmas.firebase.getAll
import com.deange.nastychristmas.firebase.update
import com.deange.nastychristmas.model.OwnedGift
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

  private suspend fun saveGameState(gameState: GameState?) {
    val (currentPlayer, round) = when (val state = gameState?.appState) {
      is InitializingPlayers -> null to 0
      is PickingPlayer -> state.selectedPlayer?.name to state.round
      is OpeningGift -> state.player.name to state.round
      is StealingRound -> state.startingPlayer.name to state.round
      is EndGame -> null to 0
      is ChangeGameSettings -> state.player.name to state.round
      null -> null to 0
    }

    firestore.update("current-state/default/", CurrentStateSnapshot(currentPlayer, round))

    val oldGifts: Set<GiftSnapshot> = firestore.getAll<GiftSnapshot>("gifts").toSet()
    val oldGiftIds = oldGifts.associateBy { it.id }

    val newGifts: Set<GiftSnapshot> = when (val state = gameState?.appState) {
      is InitializingPlayers -> null
      is PickingPlayer -> state.gifts
      is OpeningGift -> state.gifts
      is StealingRound -> state.gifts
      is EndGame -> null
      is ChangeGameSettings -> state.gifts
      null -> null
    }?.map { it.value.toGiftSnapshot() }.orEmpty().toSet()
    val newGiftIds = newGifts.associateBy { it.id }

    val giftsToAdd = (newGiftIds - oldGiftIds.keys).values.toSet()
    val giftsToRemove = (oldGiftIds - newGiftIds.keys).values.toSet()
    val giftsToUpdate = newGifts.filter { gift ->
      gift.id in oldGiftIds && gift.id in newGiftIds && oldGiftIds[gift.id] != newGiftIds[gift.id]
    }.toSet()

    firestore.batch(
      adds = giftsToAdd.associateBy { "gifts/${it.id}" },
      updates = giftsToUpdate.associateBy { "gifts/${it.id}" },
      deletes = giftsToRemove.map { "gifts/${it.id}" }.toSet(),
    )
  }

  private companion object {
    fun OwnedGift.toGiftSnapshot(): GiftSnapshot {
      return GiftSnapshot(
        gift = gift.name,
        owner = owner.name,
        owners = owners.map { it.name }.toSet(),
      )
    }
  }
}
