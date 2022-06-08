package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class StealingRoundState(
  val currentPlayer: Player,
  val gifts: GiftOwners,
  val currentChoice: PlayerChoice?,
  val previousState: StealingRoundState?,
) {
  fun withSteal(victim: Player): StealingRoundState {
    return copy(
      currentPlayer = victim,
      gifts = gifts.withSteal(
        stealer = currentPlayer,
        victim = victim,
      ),
      currentChoice = null,
      previousState = this,
    )
  }
}

@Serializable
sealed class PlayerChoice {
  @Serializable
  object OpenNewGift : PlayerChoice()

  @Serializable
  data class StealGiftFrom(
    val victim: Player,
  ) : PlayerChoice()
}
