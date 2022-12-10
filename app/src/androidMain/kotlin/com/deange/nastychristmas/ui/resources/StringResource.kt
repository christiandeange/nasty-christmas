package com.deange.nastychristmas.ui.resources

actual class StringResource actual constructor(
  private val formatString: String,
) {
  actual fun evaluate(vararg formatArgs: Any): String {
    return formatString.format(*formatArgs)
  }
}
