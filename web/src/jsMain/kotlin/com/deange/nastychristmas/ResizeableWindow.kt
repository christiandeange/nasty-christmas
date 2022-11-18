// calling internal functions ComposeLayer and ComposeWindow
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "EXPOSED_PARAMETER_TYPE")

package com.deange.nastychristmas

import androidx.compose.runtime.Composable
import androidx.compose.ui.native.ComposeLayer
import androidx.compose.ui.window.ComposeWindow
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.Window

@Suppress("FunctionName")
fun ResizeableWindow(
  window: Window,
  title: String = "JetpackNativeWindow",
  content: @Composable () -> Unit = { },
) {
  val composeWindow = ComposeWindow()
  val canvas = composeWindow.canvas

  canvas.resize()

  ComposeWindow().apply {
    setTitle(title)
    window.addEventListener("resize", { composableResize(layer, canvas) })
    setContent(content)
  }
}

fun HTMLElement.resize(
  width: Int = window.innerWidth,
  height: Int = window.innerHeight,
) {
  setAttribute("width", "$width")
  setAttribute("height", "$height")
}

fun composableResize(
  layer: ComposeLayer,
  canvas: HTMLCanvasElement,
) {
  val scale = layer.layer.contentScale
  canvas.resize()
  layer.layer.attachTo(canvas)
  layer.layer.needRedraw()
  layer.setSize(
    (canvas.width / scale).toInt(),
    (canvas.height / scale).toInt()
  )
}
