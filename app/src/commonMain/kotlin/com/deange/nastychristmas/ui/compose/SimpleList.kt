package com.deange.nastychristmas.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deange.nastychristmas.ui.resources.StringResource

@Composable
fun SimpleList(
  title: SimpleText,
  subtitle: SimpleText? = null,
  items: List<SimpleListItem> = emptyList(),
) {
  AppScaffold(
    onBack = BackBehaviour.Hidden,
    title = {
      if (subtitle == null) {
        Text(
          text = title.evaluate(),
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
      } else {
        TwoLineText(
          title = title.evaluate(),
          description = title.evaluate(),
        )
      }
    },
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 16.dp)
    ) {
      LazyColumn(modifier = Modifier.weight(1f)) {
        items(items, key = { it.hashCode() }) { item ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .minimumInteractiveComponentSize()
              .padding(horizontal = 16.dp),
          ) {
            when (item) {
              is SimpleListItem.OneLine -> {
                Text(text = item.text.evaluate())
              }
              is SimpleListItem.TwoLine -> {
                TwoLineText(
                  title = item.text.evaluate(),
                  description = item.subtext.evaluate(),
                )
              }
            }
          }
        }
      }
    }
  }
}

data class SimpleText(
  val text: StringResource,
  val args: List<Any>,
) {
  constructor(text: StringResource, vararg args: Any) : this(text, args.toList())

  constructor(text: String) : this(StringResource(text), emptyList())

  fun evaluate(): String {
    return text.evaluate(*args.toTypedArray())
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as SimpleText

    if (text != other.text) return false
    if (args != other.args) return false

    return true
  }

  override fun hashCode(): Int {
    var result = text.hashCode()
    result = 31 * result + args.hashCode()
    return result
  }
}

sealed class SimpleListItem {
  data class OneLine(
    val text: SimpleText,
  ) : SimpleListItem()

  data class TwoLine(
    val text: SimpleText,
    val subtext: SimpleText,
  ) : SimpleListItem()
}
