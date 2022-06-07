package com.deange.nastychristmas.settings

import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
class GameSettingsWorkflow : StatefulWorkflow<
    ChangeableSettings,
    GameSettingsState,
    ChangeableSettings,
    ViewRendering
    >() {

  override fun initialState(props: ChangeableSettings, snapshot: Snapshot?): GameSettingsState {
    return GameSettingsState.serializer().fromSnapshot(snapshot)
      ?: GameSettingsState(
        enforceOwnership = props.settings.enforceOwnership,
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
      onCancel = context.eventHandler {
        setOutput(props)
      },
      onConfirmSettings = context.eventHandler {
        setOutput(state.asSettings(props.gifts))
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
