package com.deange.nastychristmas.ui.compose.konfetti.models


sealed interface Shape {
  object Circle : Shape

  object Square : Shape

  class Rectangle(
    /** The ratio of height to width. Must be within range [0, 1] */
    val heightRatio: Float
  ) : Shape {
    init {
      require(heightRatio in 0f..1f)
    }
  }
}
