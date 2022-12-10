package com.deange.nastychristmas.ui.resources

import kotlinx.browser.window
import kotlin.js.Json
import kotlin.js.json

actual class StringResource actual constructor(
  private val formatString: String,
) {
  private val messageFormatRegex = "%(.)(?:\\\$(.))?".toRegex()

  actual fun evaluate(vararg formatArgs: Any): String {
    val jsFormatString = formatString.convertToMessageFormat()
    val compiled = CompiledVariableString(
      MessageFormat(window.navigator.languages as Array<String>).compile(jsFormatString)
    )
    return compiled.evaluate(*formatArgs)
  }

  fun String.convertToMessageFormat(): String {
    val allMatches = messageFormatRegex
      .findAll(this)

    if (allMatches.count() == 0) return this

    var counter = 0
    var result = this

    // First go through the positioned args
    allMatches
      .filter { matchResult -> matchResult.groupValues[2].isNotEmpty() }
      .map { matchResult -> matchResult.groupValues[0] to matchResult.groupValues[1] }
      .distinctBy { it.second }
      .forEach { (wholeMatch, index) ->
        val intIndex = index.toIntOrNull() ?: error("Localized string $this uses positioned " +
            "argument $wholeMatch but $index is not an integer.")

        result = result.replace(wholeMatch, "{${intIndex - 1}}")
        counter = intIndex
      }

    // Now remove the not positioned args
    while (messageFormatRegex.containsMatchIn(result)) {
      result = messageFormatRegex.replaceFirst(result, "{$counter}")
      counter++
    }

    return result
  }

}

private value class CompiledVariableString(private val function: (Json) -> String) {
  fun evaluate(vararg args: Any): String {
    val keyValues = args.mapIndexed { index: Int, any: Any -> "$index" to any }

    val json = json(*keyValues.toTypedArray())
    return function(json)
  }
}

@JsModule("@messageformat/core")
@JsNonModule
private external class MessageFormat(locales: Array<String>) {
  fun compile(format: String): (Json) -> String
}
