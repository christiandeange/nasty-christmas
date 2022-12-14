package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.round.PlayerChoice.OpenNewGift
import com.deange.nastychristmas.round.PlayerChoice.StealGiftFrom
import com.deange.nastychristmas.round.StealingRoundOutput.ChangeGameSettings
import com.deange.nastychristmas.round.StealingRoundOutput.EndRound
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

class StealingRoundWorkflow : StatefulWorkflow<
    StealingRoundProps,
    StealingRoundState,
    StealingRoundOutput,
    ViewRendering
    >() {

  override fun initialState(props: StealingRoundProps, snapshot: Snapshot?): StealingRoundState {
    return StealingRoundState.serializer().fromSnapshot(snapshot)
      ?: StealingRoundState(
        currentPlayer = props.startingPlayer,
        gifts = props.gifts,
        stats = GameStats(),
        currentChoice = null,
        previousState = null,
      )
  }

  override fun onPropsChanged(
    old: StealingRoundProps,
    new: StealingRoundProps,
    state: StealingRoundState
  ): StealingRoundState {
    return super.onPropsChanged(old, new, state).copy(
      gifts = new.gifts,
    )
  }

  override fun render(
    renderProps: StealingRoundProps,
    renderState: StealingRoundState,
    context: RenderContext
  ): ViewRendering {
    val choices = buildList {
      add(
        StealOrOpenChoice.Open(
          isSelected = renderState.currentChoice is OpenNewGift,
          onPicked = context.eventHandler {
            state = state.copy(currentChoice = OpenNewGift)
          }
        )
      )

      val allowAnySteal = !renderProps.settings.enforceOwnership

      renderState.gifts.forEach { (player, gift) ->
        val isCurrentChoice = (renderState.currentChoice as? StealGiftFrom)?.victim == player
        val alreadyOwnedThisRound = renderState.currentPlayer in gift.owners

        add(
          StealOrOpenChoice.Steal(
            playerName = player.name,
            giftName = gift.gift.name,
            isSelected = isCurrentChoice,
            isEnabled = allowAnySteal || !alreadyOwnedThisRound,
            onPicked = context.eventHandler {
              state = state.copy(currentChoice = StealGiftFrom(player))
            }
          )
        )
      }
    }

    return StealingRoundScreen(
      playerName = renderState.currentPlayer.name,
      roundNumber = renderProps.roundNumber,
      choices = choices,
      onChangeSettings = context.eventHandler {
        setOutput(
          ChangeGameSettings(
            currentPlayer = state.currentPlayer,
            gifts = state.gifts,
            stats = state.stats,
          )
        )
      },
      onUndo = context.eventHandler {
        state.previousState?.let { previous ->
          state = previous
        }
      }.takeIf { renderState.previousState != null },
      onConfirmChoice = context.eventHandler {
        when (val currentChoice: PlayerChoice = state.currentChoice!!) {
          is OpenNewGift -> {
            val stats = state.stats.withOpen(state.currentPlayer)
            setOutput(
              EndRound(
                playerOpeningGift = state.currentPlayer,
                gifts = state.gifts,
                stats = stats,
              )
            )
          }
          is StealGiftFrom -> {
            state = state.withSteal(victim = currentChoice.victim)
          }
        }
      },
    )
  }

  override fun snapshotState(state: StealingRoundState): Snapshot {
    return StealingRoundState.serializer().toSnapshot(state)
  }
}
