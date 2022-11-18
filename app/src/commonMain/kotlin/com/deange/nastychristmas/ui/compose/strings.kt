package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource

@Composable
expect fun StringResource.evaluate(vararg formatArgs: Any): String
