@file:OptIn(InternalSerializationApi::class)

package com.deange.nastychristmas.firebase

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer

interface Firestore {
  suspend fun <T : Any> getAll(path: String, strategy: DeserializationStrategy<T>): List<T>

  suspend fun <T : Any> get(path: String, strategy: DeserializationStrategy<T>): T

  fun <T : Any> observeAll(path: String, strategy: DeserializationStrategy<T>): Flow<List<T>>

  fun <T : Any> observe(path: String, strategy: DeserializationStrategy<T>): Flow<T?>

  suspend fun <T : Any> add(path: String, strategy: SerializationStrategy<T>, data: T)

  suspend fun <T : Any> update(path: String, strategy: SerializationStrategy<T>, data: T)

  suspend fun delete(path: String)

  suspend fun <T : Any> batch(
    adds: Map<String, T>,
    updates: Map<String, T>,
    deletes: Set<String>,
    strategy: SerializationStrategy<T>,
  )
}

suspend inline fun <reified T : Any> Firestore.getAll(path: String): List<T> {
  return getAll(path, T::class.serializer())
}

suspend inline fun <reified T : Any> Firestore.get(path: String): T {
  return get(path, T::class.serializer())
}

inline fun <reified T : Any> Firestore.observeAll(path: String): Flow<List<T>> {
  return observeAll(path, T::class.serializer())
}

inline fun <reified T : Any> Firestore.observe(path: String): Flow<T?> {
  return observe(path, T::class.serializer())
}

suspend inline fun <reified T : Any> Firestore.add(path: String, data: T) {
  return add(path, T::class.serializer(), data)
}

suspend inline fun <reified T : Any> Firestore.update(path: String, data: T) {
  return update(path, T::class.serializer(), data)
}

suspend inline fun <reified T : Any> Firestore.batch(
  adds: Map<String, T>,
  updates: Map<String, T>,
  deletes: Set<String>,
) {
  batch(adds, updates, deletes, T::class.serializer())
}
