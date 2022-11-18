package com.deange.nastychristmas.model

import kotlinx.serialization.Serializable

@Serializable
data class OwnedGift(
  val gift: Gift,
  val owner: Player,
  val owners: Set<Player>,
) {
  fun stolenBy(player: Player): OwnedGift {
    return copy(owner = player, owners = owners + player)
  }

  fun clearOwnerHistory(): OwnedGift {
    return copy(owners = setOf(owner))
  }
}

fun Player.owns(gift: Gift): OwnedGift {
  return OwnedGift(
    gift = gift,
    owner = this,
    owners = setOf(this),
  )
}
