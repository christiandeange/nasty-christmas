package com.deange.nastychristmas.settings

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.settings.GameSettingsOutput.ResetGame
import com.deange.nastychristmas.settings.GameSettingsOutput.UpdateGameSettings
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

class GameSettingsWorkflow : StatefulWorkflow<
    ChangeableSettings,
    GameSettingsState,
    GameSettingsOutput,
    ViewRendering
    >() {

  override fun initialState(props: ChangeableSettings, snapshot: Snapshot?): GameSettingsState {
    return GameSettingsState.serializer().fromSnapshot(snapshot)
      ?: GameSettingsState(
        enforceOwnership = props.settings.enforceOwnership,
        showConfirmResetGame = false,
        giftNames = props.gifts.map { (_, ownedGift) ->
          GiftName(ownedGift, TextController(ownedGift.gift.name))
        }
      )
  }

  override fun render(
    renderProps: ChangeableSettings,
    renderState: GameSettingsState,
    context: RenderContext
  ): ViewRendering {
    return GameSettingsScreen(
      enforceOwnership = renderState.enforceOwnership,
      onEnforceOwnershipChanged = context.eventHandler { enforceOwnership ->
        state = state.copy(enforceOwnership = enforceOwnership)
      },
      giftNames = renderState.giftNames.asRows(),
      showConfirmResetGame = renderState.showConfirmResetGame,
      onResetGame = context.eventHandler {
        if (!state.showConfirmResetGame) {
          state = state.copy(showConfirmResetGame = true)
        } else {
          setOutput(ResetGame)
        }
      },
      onBack = context.eventHandler {
        setOutput(UpdateGameSettings(props))
      },
      onConfirmSettings = context.eventHandler {
        val newGiftNames = state.giftNames.map { it.newName.textValue }
        if (newGiftNames.size == newGiftNames.distinct().size) {
          // Only output new settings if all gift names are different.
          setOutput(
            UpdateGameSettings(
              state.asSettings(
                gifts = props.gifts,
                stats = props.stats,
                roundStats = props.roundStats,
              )
            )
          )
        }
      },
    )
  }

  override fun snapshotState(state: GameSettingsState): Snapshot {
    return GameSettingsState.serializer().toSnapshot(state)
  }

  private fun List<GiftName>.asRows(): List<GiftNameRow> {
    return map { GiftNameRow(it.gift, it.newName) }
  }

  private fun GameSettingsState.asSettings(
    gifts: GiftOwners,
    stats: GameStats,
    roundStats: GameStats,
  ): ChangeableSettings {
    return ChangeableSettings(
      settings = GameSettings(
        enforceOwnership = enforceOwnership,
      ),
      gifts = GiftOwners(
        owners = gifts.associate { (player, ownedGift) ->
          player to ownedGift.copy(
            gift = ownedGift.gift.copy(
              name = newGiftName(ownedGift.gift.name),
            )
          )
        }
      ),
      stats = stats.copy(
        stealsByGift = stats.stealsByGift.mapKeys { (giftName, _) -> newGiftName(giftName) },
      ),
      roundStats = roundStats.copy(
        stealsByGift = roundStats.stealsByGift.mapKeys { (giftName, _) -> newGiftName(giftName) },
      ),
    )
  }

  private fun GameSettingsState.newGiftName(oldGiftName: String): String {
    return giftNames.single { it.gift.gift.name == oldGiftName }.newName.textValue
  }
}
