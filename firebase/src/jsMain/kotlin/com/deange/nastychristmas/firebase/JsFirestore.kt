package com.deange.nastychristmas.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

class JsFirestore : Firestore {
  private val store by lazy { Firebase.firestore }

  override suspend fun <T : Any> getAll(path: String, strategy: DeserializationStrategy<T>): List<T> {
    return store.collection(path).get().documents.map { it.data(strategy) }
  }

  override suspend fun <T : Any> get(path: String, strategy: DeserializationStrategy<T>): T {
    return store.document(path).get().data(strategy)
  }

  override suspend fun exists(path: String): Boolean {
    return store.document(path).get().exists
  }

  override fun <T : Any> observeAll(path: String, strategy: DeserializationStrategy<T>): Flow<List<T>> {
    return store.collection(path).snapshots.map {
      it.documents.mapNotNull { snapshot ->
        if (snapshot.exists) snapshot.data(strategy) else null
      }
    }
  }

  override fun <T : Any> observe(path: String, strategy: DeserializationStrategy<T>): Flow<T?> {
    return store.document(path).snapshots.map { snapshot ->
      if (snapshot.exists) snapshot.data(strategy) else null
    }
  }

  override suspend fun <T : Any> add(path: String, strategy: SerializationStrategy<T>, data: T) {
    store.document(path).set(strategy, data)
  }

  override suspend fun <T : Any> update(path: String, strategy: SerializationStrategy<T>, data: T) {
    store.document(path).update(strategy, data)
  }

  override suspend fun delete(path: String) {
    store.document(path).delete()
  }

  override suspend fun <T : Any> batch(
    adds: Map<String, T>,
    updates: Map<String, T>,
    deletes: Set<String>,
    strategy: SerializationStrategy<T>,
  ) {
    store.batch().apply {
      adds.forEach { (path, data) ->
        set(store.document(path), strategy, data)
      }
      updates.forEach { (path, data) ->
        update(store.document(path), strategy, data)
      }
      deletes.forEach { path ->
        delete(store.document(path))
      }
    }.commit()
  }
}
