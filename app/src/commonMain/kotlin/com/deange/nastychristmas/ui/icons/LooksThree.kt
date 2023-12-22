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

val Icons.Filled.LooksThree: ImageVector
  get() {
    if (_looksThree != null) {
      return _looksThree!!
    }
    _looksThree = materialIcon(name = "Filled.LooksThree") {
      materialPath {
        moveTo(19.01f, 3.0f)
        horizontalLineToRelative(-14.0f)
        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(14.0f)
        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(14.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        lineTo(21.01f, 5.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(15.01f, 10.5f)
        curveToRelative(0.0f, 0.83f, -0.67f, 1.5f, -1.5f, 1.5f)
        curveToRelative(0.83f, 0.0f, 1.5f, 0.67f, 1.5f, 1.5f)
        lineTo(15.01f, 15.0f)
        curveToRelative(0.0f, 1.11f, -0.9f, 2.0f, -2.0f, 2.0f)
        horizontalLineToRelative(-4.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(4.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(-2.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(2.0f)
        lineTo(13.01f, 9.0f)
        horizontalLineToRelative(-4.0f)
        lineTo(9.01f, 7.0f)
        horizontalLineToRelative(4.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, 0.89f, 2.0f, 2.0f)
        verticalLineToRelative(1.5f)
        close()
      }
    }
    return _looksThree!!
  }

private var _looksThree: ImageVector? = null
