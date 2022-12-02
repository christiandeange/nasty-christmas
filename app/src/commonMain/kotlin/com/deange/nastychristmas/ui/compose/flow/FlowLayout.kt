@file:Suppress("unused")

package com.deange.nastychristmas.ui.compose.flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * A composable that places its children in a horizontal flow. Unlike [Row], if the
 * horizontal space is too small to put all the children in one row, multiple rows may be used.
 *
 * Note that just like [Row], flex values cannot be used with [FlowRow].
 *
 * @param modifier The modifier to be applied to the FlowRow.
 * @param mainAxisSize The size of the layout in the main axis direction.
 * @param mainAxisAlignment The alignment of each row's children in the main axis direction.
 * @param mainAxisSpacing The main axis spacing between the children of each row.
 * @param crossAxisAlignment The alignment of each row's children in the cross axis direction.
 * @param crossAxisSpacing The cross axis spacing between the rows of the layout.
 * @param lastLineMainAxisAlignment Overrides the main axis alignment of the last row.
 */
@Composable
fun FlowRow(
  modifier: Modifier = Modifier,
  mainAxisSize: SizeMode = SizeMode.Wrap,
  mainAxisAlignment: FlowMainAxisAlignment = FlowMainAxisAlignment.Start,
  mainAxisSpacing: Dp = 0.dp,
  crossAxisAlignment: FlowCrossAxisAlignment = FlowCrossAxisAlignment.Start,
  crossAxisSpacing: Dp = 0.dp,
  lastLineMainAxisAlignment: FlowMainAxisAlignment = mainAxisAlignment,
  content: @Composable () -> Unit
) {
  Flow(
    modifier = modifier,
    orientation = LayoutOrientation.Horizontal,
    mainAxisSize = mainAxisSize,
    mainAxisAlignment = mainAxisAlignment,
    mainAxisSpacing = mainAxisSpacing,
    crossAxisAlignment = crossAxisAlignment,
    crossAxisSpacing = crossAxisSpacing,
    lastLineMainAxisAlignment = lastLineMainAxisAlignment,
    content = content
  )
}

/**
 * A composable that places its children in a vertical flow. Unlike [Column], if the
 * vertical space is too small to put all the children in one column, multiple columns may be used.
 *
 * Note that just like [Column], flex values cannot be used with [FlowColumn].
 *
 * @param modifier The modifier to be applied to the FlowColumn.
 * @param mainAxisSize The size of the layout in the main axis direction.
 * @param mainAxisAlignment The alignment of each column's children in the main axis direction.
 * @param mainAxisSpacing The main axis spacing between the children of each column.
 * @param crossAxisAlignment The alignment of each column's children in the cross axis direction.
 * @param crossAxisSpacing The cross axis spacing between the columns of the layout.
 * @param lastLineMainAxisAlignment Overrides the main axis alignment of the last column.
 */
@Composable
fun FlowColumn(
  modifier: Modifier = Modifier,
  mainAxisSize: SizeMode = SizeMode.Wrap,
  mainAxisAlignment: FlowMainAxisAlignment = FlowMainAxisAlignment.Start,
  mainAxisSpacing: Dp = 0.dp,
  crossAxisAlignment: FlowCrossAxisAlignment = FlowCrossAxisAlignment.Start,
  crossAxisSpacing: Dp = 0.dp,
  lastLineMainAxisAlignment: FlowMainAxisAlignment = mainAxisAlignment,
  content: @Composable () -> Unit
) {
  Flow(
    modifier = modifier,
    orientation = LayoutOrientation.Vertical,
    mainAxisSize = mainAxisSize,
    mainAxisAlignment = mainAxisAlignment,
    mainAxisSpacing = mainAxisSpacing,
    crossAxisAlignment = crossAxisAlignment,
    crossAxisSpacing = crossAxisSpacing,
    lastLineMainAxisAlignment = lastLineMainAxisAlignment,
    content = content
  )
}

/**
 * Used to specify the alignment of a layout's children, in cross axis direction.
 */
enum class FlowCrossAxisAlignment {
  /**
   * Place children such that their center is in the middle of the cross axis.
   */
  Center,
  /**
   * Place children such that their start edge is aligned to the start edge of the cross axis.
   */
  Start,
  /**
   * Place children such that their end edge is aligned to the end edge of the cross axis.
   */
  End,
}

/**
 * Layout model that arranges its children in a horizontal or vertical flow.
 */
@Composable
private fun Flow(
  modifier: Modifier,
  orientation: LayoutOrientation,
  mainAxisSize: SizeMode,
  mainAxisAlignment: FlowMainAxisAlignment,
  mainAxisSpacing: Dp,
  crossAxisAlignment: FlowCrossAxisAlignment,
  crossAxisSpacing: Dp,
  lastLineMainAxisAlignment: FlowMainAxisAlignment,
  content: @Composable () -> Unit
) {
  fun Placeable.mainAxisSize() =
    if (orientation == LayoutOrientation.Horizontal) width else height

  fun Placeable.crossAxisSize() =
    if (orientation == LayoutOrientation.Horizontal) height else width

  Layout(content, modifier) { measurables, outerConstraints ->
    val sequences = mutableListOf<List<Placeable>>()
    val crossAxisSizes = mutableListOf<Int>()
    val crossAxisPositions = mutableListOf<Int>()

    var mainAxisSpace = 0
    var crossAxisSpace = 0

    val currentSequence = mutableListOf<Placeable>()
    var currentMainAxisSize = 0
    var currentCrossAxisSize = 0

    val constraints = OrientationIndependentConstraints(outerConstraints, orientation)

    val childConstraints = if (orientation == LayoutOrientation.Horizontal) {
      Constraints(maxWidth = constraints.mainAxisMax)
    } else {
      Constraints(maxHeight = constraints.mainAxisMax)
    }

    // Return whether the placeable can be added to the current sequence.
    fun canAddToCurrentSequence(placeable: Placeable) =
      currentSequence.isEmpty() || currentMainAxisSize + mainAxisSpacing.roundToPx() +
          placeable.mainAxisSize() <= constraints.mainAxisMax

    // Store current sequence information and start a new sequence.
    fun startNewSequence() {
      if (sequences.isNotEmpty()) {
        crossAxisSpace += crossAxisSpacing.roundToPx()
      }
      sequences += currentSequence.toList()
      crossAxisSizes += currentCrossAxisSize
      crossAxisPositions += crossAxisSpace

      crossAxisSpace += currentCrossAxisSize
      mainAxisSpace = max(mainAxisSpace, currentMainAxisSize)

      currentSequence.clear()
      currentMainAxisSize = 0
      currentCrossAxisSize = 0
    }

    for (measurable in measurables) {
      // Ask the child for its preferred size.
      val placeable = measurable.measure(childConstraints)

      // Start a new sequence if there is not enough space.
      if (!canAddToCurrentSequence(placeable)) startNewSequence()

      // Add the child to the current sequence.
      if (currentSequence.isNotEmpty()) {
        currentMainAxisSize += mainAxisSpacing.roundToPx()
      }
      currentSequence.add(placeable)
      currentMainAxisSize += placeable.mainAxisSize()
      currentCrossAxisSize = max(currentCrossAxisSize, placeable.crossAxisSize())
    }

    if (currentSequence.isNotEmpty()) startNewSequence()

    val mainAxisLayoutSize = if (constraints.mainAxisMax != Constraints.Infinity &&
      mainAxisSize == SizeMode.Expand
    ) {
      constraints.mainAxisMax
    } else {
      max(mainAxisSpace, constraints.mainAxisMin)
    }
    val crossAxisLayoutSize = max(crossAxisSpace, constraints.crossAxisMin)

    val layoutWidth = if (orientation == LayoutOrientation.Horizontal) {
      mainAxisLayoutSize
    } else {
      crossAxisLayoutSize
    }
    val layoutHeight = if (orientation == LayoutOrientation.Horizontal) {
      crossAxisLayoutSize
    } else {
      mainAxisLayoutSize
    }

    layout(layoutWidth, layoutHeight) {
      sequences.forEachIndexed { i, placeables ->
        val childrenMainAxisSizes = IntArray(placeables.size) { j ->
          placeables[j].mainAxisSize() +
              if (j < placeables.lastIndex) mainAxisSpacing.roundToPx() else 0
        }
        val arrangement = if (i < sequences.lastIndex) {
          mainAxisAlignment.arrangement
        } else {
          lastLineMainAxisAlignment.arrangement
        }
        // TODO(soboleva): rtl support
        // Handle vertical direction
        val mainAxisPositions = IntArray(childrenMainAxisSizes.size) { 0 }
        with(arrangement) {
          arrange(mainAxisLayoutSize, childrenMainAxisSizes, mainAxisPositions)
        }
        placeables.forEachIndexed { j, placeable ->
          val crossAxis = when (crossAxisAlignment) {
            FlowCrossAxisAlignment.Start -> 0
            FlowCrossAxisAlignment.End ->
              crossAxisSizes[i] - placeable.crossAxisSize()
            FlowCrossAxisAlignment.Center ->
              Alignment.Center.align(
                IntSize.Zero,
                IntSize(
                  width = 0,
                  height = crossAxisSizes[i] - placeable.crossAxisSize()
                ),
                LayoutDirection.Ltr
              ).y
          }
          if (orientation == LayoutOrientation.Horizontal) {
            placeable.place(
              x = mainAxisPositions[j],
              y = crossAxisPositions[i] + crossAxis
            )
          } else {
            placeable.place(
              x = crossAxisPositions[i] + crossAxis,
              y = mainAxisPositions[j]
            )
          }
        }
      }
    }
  }
}

enum class SizeMode {
  Wrap,
  Expand
}

enum class FlowMainAxisAlignment(internal val arrangement: Arrangement.Vertical) {
  Center(Arrangement.Center),
  Start(Arrangement.Top),
  End(Arrangement.Bottom),
  SpaceEvenly(Arrangement.SpaceEvenly),
  SpaceBetween(Arrangement.SpaceBetween),
  SpaceAround(Arrangement.SpaceAround);
}
