package com.deange.nastychristmas.firebase

import kotlinx.serialization.Serializable

@Serializable
data class GiftSnapshot(
  val gift: String,
  val owner: String,
  val owners: Set<String>,
) {
  val id: String
    get() = gift.replace(Regex("[^a-zA-Z0-9]"), "-")
}
