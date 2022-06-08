package com.deange.nastychristmas.storage

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class GameStateStorage(application: Application) {
  private val dataStore: DataStore<Preferences> = application.dataStore
  private val key = stringPreferencesKey("game-state")

  suspend fun get(): GameState? {
    return dataStore.data.first()[key]?.let { encodedData ->
      GameState.serializer().fromString(encodedData)
    }
  }

  suspend fun set(state: GameState) {
    dataStore.edit { preferences ->
      preferences[key] = GameState.serializer().toString(state)
    }
  }

  suspend fun clear() {
    dataStore.edit { preferences ->
      preferences.remove(key)
    }
  }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private fun <T> KSerializer<T>.toString(value: T): String {
  return Json.encodeToString(this, value)
}

private fun <T> KSerializer<T>.fromString(string: String): T {
  return Json.decodeFromString(this, string)
}
