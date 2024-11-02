package com.deange.nastychristmas.registry

import com.deange.nastychristmas.firebase.Firestore
import com.squareup.workflow1.Worker
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
class CollectionWorker<T : Any>(
  private val firestore: Firestore,
  private val path: String,
  private val kClazz: KClass<T>,
) : Worker<List<T>> {
  override fun run(): Flow<List<T>> {
    return firestore.observeAll(path, kClazz.serializer())
  }

  override fun doesSameWorkAs(otherWorker: Worker<*>): Boolean {
    return (otherWorker as? CollectionWorker<*>)?.path == path
  }
}
