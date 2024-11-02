package com.deange.nastychristmas.registry

import com.deange.nastychristmas.firebase.Firestore
import com.squareup.workflow1.Worker
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
class DocumentWorker<T : Any>(
  private val firestore: Firestore,
  private val path: String,
  private val kClazz: KClass<T>,
) : Worker<T?> {
  override fun run(): Flow<T?> {
    return firestore.observe(path, kClazz.serializer())
  }

  override fun doesSameWorkAs(otherWorker: Worker<*>): Boolean {
    return (otherWorker as? DocumentWorker<*>)?.path == path
  }
}
