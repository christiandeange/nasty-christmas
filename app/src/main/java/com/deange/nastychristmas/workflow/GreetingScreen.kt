package com.deange.nastychristmas.workflow

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.deange.nastychristmas.ui.workflow.ViewEnvironment
import com.deange.nastychristmas.ui.workflow.ViewRendering

class GreetingScreen(
  val text: String,
) : ViewRendering {
  @Composable
  override fun View(viewEnvironment: ViewEnvironment) {
    Greeting(text)
  }
}

@Composable
private fun Greeting(name: String) {
  Text(text = "Hello $name!")
}
