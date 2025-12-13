package com.deange.nastychristmas

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.firebase.Firebase
import com.deange.nastychristmas.firebase.initializeFirebase
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.state.GameSaver
import com.deange.nastychristmas.state.GameStateFactory
import com.deange.nastychristmas.store.DataStoreStorage
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.workflow.AppWorkflow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random
import kotlin.time.Clock

class MainApplication : Application() {
  val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

  val storage: PersistentStorage by lazy {
    DataStoreStorage(dataStore)
  }

  val firebase: Firebase by lazy {
    initializeFirebase(this)
  }

  val random by lazy {
    Random(seed = System.currentTimeMillis())
  }

  val gameSaver: GameSaver by lazy {
    GameSaver(storage, firebase.firestore, CoroutineScope(Dispatchers.IO))
  }

  val gameStateFactory by lazy {
    GameStateFactory(Clock.System)
  }

  val workflow by lazy {
    AppWorkflow(
      playersWorkflow = PlayersWorkflow(random = random, gameSaver = gameSaver),
      newRoundWorkflow = NewRoundWorkflow(),
      openGiftWorkflow = OpenGiftWorkflow(),
      stealingRoundWorkflow = StealingRoundWorkflow(storage),
      endGameWorkflow = EndGameWorkflow(),
      gameSettingsWorkflow = GameSettingsWorkflow(),
      gameStateFactory = gameStateFactory,
      random = random,
    )
  }
}
