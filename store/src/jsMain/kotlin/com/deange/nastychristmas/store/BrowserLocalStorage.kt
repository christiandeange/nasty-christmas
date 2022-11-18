package com.deange.nastychristmas.store

import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.w3c.dom.Storage
import org.w3c.dom.get
import org.w3c.dom.set

class BrowserLocalStorage(
  internal val storage: Storage,
  private val keyPrefix: String,
) : PersistentStorage {
  private val keyCache: Map<String, Preference<*>> = mutableMapOf()

  override fun <T> preference(
    key: String,
    defaultValue: T,
    serializer: Serializer<T>
  ): Preference<T> {
    val keyName = "${keyPrefix}.${key}"

    @Suppress("UNCHECKED_CAST")
    return keyCache.getOrElse(keyName) {
      BrowserStoragePreference(
        key = keyName,
        storage = this,
        serializer = serializer,
        defaultValue = defaultValue,
      )
    } as Preference<T>
  }

  override fun clear() {
    keys().forEach { key ->
      // We can't just iterate through the key cache because there may be values stored from a previous
      // session that have not been queried in this session. This must be done to trigger any subscribers
      // listening to preference changes. If there is no entry for this key, remove it from storage anyway.
      keyCache[key]?.delete()
      storage.removeItem(key)
    }
  }

  internal fun keys(): List<String> {
    return List(storage.length) { i -> storage.key(i).orEmpty() }
      .filter { it.startsWith("${keyPrefix}.") }
  }
}

private class BrowserStoragePreference<T>(
  private val key: String,
  private val storage: BrowserLocalStorage,
  private val serializer: Serializer<T>,
  private val defaultValue: T,
) : Preference<T> {
  private val channel = MutableSharedFlow<T>(
    replay = 1,
    extraBufferCapacity = 1,
    onBufferOverflow = DROP_OLDEST,
  )

  override fun get(): T {
    val value = if (key in storage.keys()) {
      storage.storage[key] ?: return defaultValue
    } else {
      return defaultValue
    }
    return serializer.deserialize(value)
  }

  override fun set(value: T) {
    storage.storage[key] = serializer.serialize(value)
    channel.tryEmit(value)
  }

  override fun delete() {
    storage.storage.removeItem(key)
    channel.tryEmit(defaultValue)
  }

  override fun asFlow(): Flow<T> = channel.asSharedFlow()
}
