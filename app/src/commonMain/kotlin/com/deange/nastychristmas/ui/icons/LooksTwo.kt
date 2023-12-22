/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deange.nastychristmas.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.LooksTwo: ImageVector
  get() {
    if (_looksTwo != null) {
      return _looksTwo!!
    }
    _looksTwo = materialIcon(name = "Filled.LooksTwo") {
      materialPath {
        moveTo(19.0f, 3.0f)
        lineTo(5.0f, 3.0f)
        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(14.0f)
        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(14.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        lineTo(21.0f, 5.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(15.0f, 11.0f)
        curveToRelative(0.0f, 1.11f, -0.9f, 2.0f, -2.0f, 2.0f)
        horizontalLineToRelative(-2.0f)
        verticalLineToRelative(2.0f)
        horizontalLineToRelative(4.0f)
        verticalLineToRelative(2.0f)
        lineTo(9.0f, 17.0f)
        verticalLineToRelative(-4.0f)
        curveToRelative(0.0f, -1.11f, 0.9f, -2.0f, 2.0f, -2.0f)
        horizontalLineToRelative(2.0f)
        lineTo(13.0f, 9.0f)
        lineTo(9.0f, 9.0f)
        lineTo(9.0f, 7.0f)
        horizontalLineToRelative(4.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, 0.89f, 2.0f, 2.0f)
        verticalLineToRelative(2.0f)
        close()
      }
    }
    return _looksTwo!!
  }

private var _looksTwo: ImageVector? = null
