package com.deange.nastychristmas.model

import kotlinx.serialization.Serializable

@Serializable(with = GiftOwnersSerializer::class)
data class GiftOwners(
  private val owners: Map<Player, OwnedGift>
) : Iterable<Map.Entry<Player, OwnedGift>> by owners.entries {

  operator fun get(player: Player): Gift? = owners[player]?.gift

  operator fun plus(ownedGift: OwnedGift): GiftOwners {
    return GiftOwners(owners + (ownedGift.owner to ownedGift))
  }

  fun withSteal(
    stealer: Player,
    victim: Player,
  ): GiftOwners {
    require(stealer !in owners) { "${stealer.name} already has a gift" }
    require(victim in owners) { "${victim.name} does not have a gift" }

    val stolenGift = owners[victim]!!.stolenBy(stealer)

    return GiftOwners(
      owners = buildMap {
        putAll(owners)
        put(stealer, stolenGift)
        remove(victim)
      }
    )
  }

  fun clearOwnerHistory(): GiftOwners {
    return GiftOwners(
      owners = owners.mapValues { (_, ownedGift) ->
        ownedGift.clearOwnerHistory()
      }
    )
  }

  fun stealableGifts(player: Player): Set<Gift> {
    return owners.values
      .filter { player !in it.owners }
      .map { it.gift }
      .toSet()
  }
}
