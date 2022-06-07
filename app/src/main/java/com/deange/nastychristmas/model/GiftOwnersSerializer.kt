package com.deange.nastychristmas.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class GiftOwnersSerializer : KSerializer<GiftOwners> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("GiftOwners", STRING)

  override fun serialize(encoder: Encoder, value: GiftOwners) {
    encoder.encodeInt(value.count())
    value.forEach { (player, ownedGift) ->
      Player.serializer().serialize(encoder, player)
      OwnedGift.serializer().serialize(encoder, ownedGift)
    }
  }

  override fun deserialize(decoder: Decoder): GiftOwners {
    val owners = buildMap {
      repeat(decoder.decodeInt()) {
        put(Player.serializer().deserialize(decoder), OwnedGift.serializer().deserialize(decoder))
      }
    }

    return GiftOwners(owners)
  }
}
