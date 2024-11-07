package com.deange.nastychristmas.model

import kotlinx.serialization.Serializable

@Serializable(with = GiftOwnersSerializer::class)
data class GiftOwners(
  private val owners: Map<String, OwnedGift>
) : Iterable<Map.Entry<String, OwnedGift>> by owners.entries {

  operator fun get(player: Player): Gift? = owners[player.name]?.gift

  operator fun plus(ownedGift: OwnedGift): GiftOwners {
    return GiftOwners(owners + (ownedGift.owner.name to ownedGift))
  }

  fun withSteal(
    stealer: Player,
    victim: Player,
  ): GiftOwners {
    require(stealer.name !in owners) { "${stealer.name} already has a gift" }
    require(victim.name in owners) { "${victim.name} does not have a gift" }

    val stolenGift = owners[victim.name]!!.stolenBy(stealer)

    return GiftOwners(
      owners = buildMap {
        putAll(owners)
        put(stealer.name, stolenGift)
        remove(victim.name)
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
