package com.deange.nastychristmas.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

class GiftOwnersSerializer : KSerializer<GiftOwners> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("GiftOwners", STRING)

  private val underlyingSerializer: KSerializer<Map<String, OwnedGift>> by lazy { serializer() }

  override fun serialize(encoder: Encoder, value: GiftOwners) {
    underlyingSerializer.serialize(encoder, value.associate { it.key to it.value })
  }

  override fun deserialize(decoder: Decoder): GiftOwners {
    return GiftOwners(underlyingSerializer.deserialize(decoder))
  }
}
