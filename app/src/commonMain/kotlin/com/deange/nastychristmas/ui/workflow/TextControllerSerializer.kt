package com.deange.nastychristmas.ui.workflow

import com.deange.nastychristmas.ui.compose.TextController
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TextControllerSerializer : KSerializer<TextController> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TextController", STRING)

  override fun serialize(encoder: Encoder, value: TextController) {
    encoder.encodeString(value.textValue)
  }

  override fun deserialize(decoder: Decoder): TextController {
    return TextController(decoder.decodeString())
  }
}
