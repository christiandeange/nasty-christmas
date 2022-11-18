package com.deange.nastychristmas.settings

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
        setOutput(UpdateGameSettings(state.asSettings(props.gifts)))
      },
    )
  }

  override fun snapshotState(state: GameSettingsState): Snapshot {
    return GameSettingsState.serializer().toSnapshot(state)
  }

  private fun List<GiftName>.asRows(): List<GiftNameRow> {
    return map { GiftNameRow(it.gift, it.newName) }
  }

  private fun GameSettingsState.asSettings(gifts: GiftOwners): ChangeableSettings {
    return ChangeableSettings(
      settings = GameSettings(enforceOwnership = enforceOwnership),
      gifts = GiftOwners(
        owners = gifts.associate { (player, ownedGift) ->
          val newGiftName = giftNames.single { it.gift == ownedGift }.newName.textValue
          player to ownedGift.copy(gift = ownedGift.gift.copy(name = newGiftName))
        }
      )
    )
  }
}
