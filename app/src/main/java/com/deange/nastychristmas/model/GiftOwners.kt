package com.deange.nastychristmas.model

import kotlinx.serialization.Serializable

@Serializable(with = GiftOwnersSerializer::class)
class GiftOwners(
  private val owners: Map<Player, OwnedGift>
) : Iterable<Map.Entry<Player, OwnedGift>> by owners.entries {

  operator fun plus(ownedGift: OwnedGift): GiftOwners {
    return GiftOwners(owners + (ownedGift.owner to ownedGift))
  }

  fun withSteal(
    stealer: Player,
    victim: Player,
  ): GiftOwners {
    require(stealer !in owners) { "$stealer already has a gift" }
    require(victim in owners) { "$victim does not have a gift" }

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

  fun canSteal(player: Player, gift: Gift): Boolean {
    val ownedGift = owners.values.single { it.gift == gift }
    return player !in ownedGift.owners
  }

  fun stealableGifts(player: Player): Set<Gift> {
    return owners.values
      .filter { player !in it.owners }
      .map { it.gift }
      .toSet()
  }
}
