package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(onBack: () -> Unit)
