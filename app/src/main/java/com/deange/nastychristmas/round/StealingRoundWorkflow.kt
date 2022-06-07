package com.deange.nastychristmas.round

import com.deange.nastychristmas.round.PlayerChoice.OpenNewGift
import com.deange.nastychristmas.round.PlayerChoice.StealGiftFrom
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
        currentChoice = null,
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

      renderState.gifts.forEach { (player, gift) ->
        add(
          StealOrOpenChoice.Steal(
            playerName = player.name,
            giftName = gift.gift.name,
            isSelected = (renderState.currentChoice as? StealGiftFrom)?.victim == player,
            isEnabled = renderState.currentPlayer !in gift.owners,
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
      onConfirmChoice = context.eventHandler {
        when (val currentChoice: PlayerChoice = state.currentChoice!!) {
          is OpenNewGift -> {
            setOutput(
              StealingRoundOutput(
                playerOpeningGift = state.currentPlayer,
                gifts = state.gifts,
              )
            )
          }
          is StealGiftFrom -> {
            state = StealingRoundState(
              currentPlayer = currentChoice.victim,
              gifts = state.gifts.withSteal(
                stealer = state.currentPlayer,
                victim = currentChoice.victim,
              ),
              currentChoice = null,
            )
          }
        }
      }
    )
  }

  override fun snapshotState(state: StealingRoundState): Snapshot {
    return StealingRoundState.serializer().toSnapshot(state)
  }
}
