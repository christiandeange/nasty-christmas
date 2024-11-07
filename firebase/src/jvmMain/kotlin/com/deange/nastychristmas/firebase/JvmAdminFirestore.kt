package com.deange.nastychristmas.firebase

import com.google.firebase.cloud.FirestoreClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

class JvmAdminFirestore : Firestore {
  private val store by lazy { FirestoreClient.getFirestore() }

  override suspend fun <T : Any> getAll(path: String, strategy: DeserializationStrategy<T>): List<T> {
    return store.collection(path).get().get().documents.map { it.data.toClass(strategy) }
  }

  override suspend fun <T : Any> get(path: String, strategy: DeserializationStrategy<T>): T {
    return store.document(path).get().get().data!!.toClass(strategy)
  }

  override fun <T : Any> observeAll(path: String, strategy: DeserializationStrategy<T>): Flow<List<T>> {
    return callbackFlow {
      val registration = store.collection(path).addSnapshotListener { value, error ->
        if (error != null) {
          close(error)
        } else {
          trySend(value?.documents?.map { it.data.toClass(strategy) }.orEmpty())
        }
      }

      awaitClose {
        registration.remove()
      }
    }
  }

  override fun <T : Any> observe(path: String, strategy: DeserializationStrategy<T>): Flow<T?> {
    return callbackFlow {
      val registration = store.document(path).addSnapshotListener { value, error ->
        if (error != null) {
          close(error)
        } else {
          trySend(value?.data?.toClass(strategy))
        }
      }

      awaitClose {
        registration.remove()
      }
    }
  }

  override suspend fun <T : Any> add(path: String, strategy: SerializationStrategy<T>, data: T) {
    store.document(path).set(data.toMap(strategy)).get()
  }

  override suspend fun <T : Any> update(path: String, strategy: SerializationStrategy<T>, data: T) {
    store.document(path).update(data.toMap(strategy)).get()
  }

  override suspend fun delete(path: String) {
    store.document(path).delete().get()
  }

  override suspend fun <T : Any> batch(
    adds: Map<String, T>,
    updates: Map<String, T>,
    deletes: Set<String>,
    strategy: SerializationStrategy<T>,
  ) {
    store.batch().apply {
      adds.forEach { (path, data) ->
        set(store.document(path), data.toMap(strategy))
      }
      updates.forEach { (path, data) ->
        update(store.document(path), data.toMap(strategy))
      }
      deletes.forEach { path ->
        delete(store.document(path))
      }
    }.commit()
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun <T : Any> Map<String, *>.toClass(strategy: DeserializationStrategy<T>): T {
    val jsonString = moshi.adapter<Map<String, *>>().toJson(this)
    return json.decodeFromString(strategy, jsonString)
  }

  @OptIn(ExperimentalStdlibApi::class)
  private fun <T : Any> T.toMap(strategy: SerializationStrategy<T>): Map<String, *> {
    val jsonString = json.encodeToString(strategy, this)
    return moshi.adapter<Map<String, *>>().fromJson(jsonString)!!
  }

  companion object {
    private val json = Json { ignoreUnknownKeys = true }

    private val moshi = Moshi.Builder().build()
  }
}
