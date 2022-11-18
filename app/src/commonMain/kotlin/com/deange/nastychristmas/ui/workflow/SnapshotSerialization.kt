package com.deange.nastychristmas.ui.workflow

import com.squareup.workflow1.Snapshot
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encodeUtf8

fun <T : Any> KSerializer<T>.toSnapshot(value: T): Snapshot {
  return Snapshot.of {
    Json.encodeToString(this, value).encodeUtf8()
  }
}

fun <T : Any> KSerializer<T>.fromSnapshot(snapshot: Snapshot?): T? {
  return snapshot?.let { Json.decodeFromString(this, it.bytes.utf8()) }
}
