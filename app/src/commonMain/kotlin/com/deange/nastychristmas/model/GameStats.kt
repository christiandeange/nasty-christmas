package com.deange.nastychristmas.model

import kotlinx.serialization.Serializable

@Serializable
data class GameStats(
  val stealsByPlayer: Map<Player, Int> = mapOf(),
  val stolenFromByPlayer: Map<Player, Int> = mapOf(),
  val opensByPlayer: Map<Player, Int> = mapOf(),
  val stealsByGift: Map<Gift, Int> = mapOf(),
) {
  operator fun plus(other: GameStats): GameStats {
    return GameStats(
      stealsByPlayer = combine(stealsByPlayer, other.stealsByPlayer),
      stolenFromByPlayer = combine(stolenFromByPlayer, other.stolenFromByPlayer),
      opensByPlayer = combine(opensByPlayer, other.opensByPlayer),
      stealsByGift = combine(stealsByGift, other.stealsByGift),
    )
  }

  fun withOpen(player: Player): GameStats {
    return copy(
      opensByPlayer = opensByPlayer.increment(player),
    )
  }

  fun withSteal(
    player: Player,
    victim: Player,
    gift: Gift,
  ): GameStats {
    return copy(
      stealsByPlayer = stealsByPlayer.increment(player),
      stolenFromByPlayer = stolenFromByPlayer.increment(victim),
      stealsByGift = stealsByGift.increment(gift),
    )
  }

  private fun <T> Map<T, Int>.increment(key: T): Map<T, Int> {
    return toMutableMap()
      .apply { put(key, (this[key] ?: 0) + 1) }
      .toMap()
  }

  private fun <T> combine(
    map1: Map<T, Int>,
    map2: Map<T, Int>,
  ): Map<T, Int> {
    val allKeys = map1.keys + map2.keys
    return allKeys.associateWith { key ->
      val value1 = map1[key] ?: 0
      val value2 = map2[key] ?: 0
      (value1 + value2)
    }
  }
}
