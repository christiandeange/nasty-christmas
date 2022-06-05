package com.deange.nastychristmas.ui.workflow

class ViewEnvironment(
  private val map: Map<ViewEnvironmentKey<*>, Any> = emptyMap()
) {
  @Suppress("UNCHECKED_CAST")
  operator fun <T : Any> get(key: ViewEnvironmentKey<T>): T {
    return map[key] as? T ?: key.default
  }

  operator fun <T : Any> plus(
    pair: Pair<ViewEnvironmentKey<T>, T>
  ): ViewEnvironment {
    return ViewEnvironment(map + (pair.first to pair.second))
  }

  operator fun plus(other: ViewEnvironment): ViewEnvironment {
    return ViewEnvironment(map + other.map)
  }

  override fun toString(): String {
    return "ViewEnvironment($map)"
  }

  override fun equals(other: Any?): Boolean {
    return map == (other as? ViewEnvironment)?.map
  }

  override fun hashCode(): Int {
    return map.hashCode()
  }
}
