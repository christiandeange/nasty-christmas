package com.deange.nastychristmas.ui.compose.flow

import androidx.compose.ui.unit.Constraints
import com.deange.nastychristmas.ui.compose.flow.LayoutOrientation.Horizontal

internal enum class LayoutOrientation {
  Horizontal,
  Vertical
}

internal data class OrientationIndependentConstraints(
  val mainAxisMin: Int,
  val mainAxisMax: Int,
  val crossAxisMin: Int,
  val crossAxisMax: Int
) {
  constructor(c: Constraints, orientation: LayoutOrientation) : this(
    if (orientation === Horizontal) c.minWidth else c.minHeight,
    if (orientation === Horizontal) c.maxWidth else c.maxHeight,
    if (orientation === Horizontal) c.minHeight else c.minWidth,
    if (orientation === Horizontal) c.maxHeight else c.maxWidth
  )
}
