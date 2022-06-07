@file:OptIn(WorkflowUiExperimentalApi::class)

package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.end.EndGameProps
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.init.PlayersOutput.NoPlayers
import com.deange.nastychristmas.init.PlayersOutput.StartWithPlayers
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.owns
import com.deange.nastychristmas.round.NewRoundProps
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftProps
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundOutput
import com.deange.nastychristmas.round.StealingRoundProps
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.ChangeableSettings
import com.deange.nastychristmas.settings.GameSettings
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.ui.workflow.BottomSheetScreen
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.deange.nastychristmas.workflow.AppState.ChangeGameSettings
import com.deange.nastychristmas.workflow.AppState.EndGame
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.deange.nastychristmas.workflow.AppState.OpeningGift
import com.deange.nastychristmas.workflow.AppState.PickingPlayer
import com.deange.nastychristmas.workflow.AppState.StealingRound
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.renderChild
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BodyAndModalsScreen

typealias AppScreen = BodyAndModalsScreen<ViewRendering, BottomSheetScreen>

class AppWorkflow(
  private val playersWorkflow: PlayersWorkflow,
  private val newRoundWorkflow: NewRoundWorkflow,
  private val openGiftWorkflow: OpenGiftWorkflow,
  private val stealingRoundWorkflow: StealingRoundWorkflow,
  private val endGameWorkflow: EndGameWorkflow,
  private val gameSettingsWorkflow: GameSettingsWorkflow,
) : StatefulWorkflow<Unit, AppState, Unit, AppScreen>() {
  override fun initialState(props: Unit, snapshot: Snapshot?): AppState {
    return AppState.serializer().fromSnapshot(snapshot)
      ?: InitializingPlayers
  }

  override fun render(
    renderProps: Unit,
    renderState: AppState,
    context: RenderContext
  ): AppScreen = when (renderState) {
    is InitializingPlayers -> {
      BodyAndModalsScreen(
        body = context.renderChild(playersWorkflow) { output ->
          action {
            when (output) {
              is NoPlayers -> setOutput(Unit)
              is StartWithPlayers -> {
                state = PickingPlayer(
                  allPlayers = output.players,
                  playerPool = output.players.toSet(),
                  round = 1,
                  gifts = GiftOwners(emptyMap()),
                  settings = GameSettings(enforceOwnership = true),
                )
              }
            }
          }
        }
      )
    }
    is PickingPlayer -> {
      val newRoundProps = NewRoundProps(
        allPlayers = renderState.allPlayers,
        playerPool = renderState.playerPool,
        roundNumber = renderState.round,
      )
      BodyAndModalsScreen(
        body = context.renderChild(newRoundWorkflow, newRoundProps) { player ->
          action {
            if (renderState.gifts.stealableGifts(player).isEmpty()) {
              state = OpeningGift(
                allPlayers = renderState.allPlayers,
                playerPool = renderState.playerPool - player,
                round = renderState.round,
                player = player,
                gifts = renderState.gifts,
                settings = renderState.settings,
              )
            } else {
              state = StealingRound(
                allPlayers = renderState.allPlayers,
                playerPool = renderState.playerPool - player,
                round = renderState.round,
                startingPlayer = player,
                gifts = renderState.gifts,
                settings = renderState.settings,
              )
            }
          }
        }
      )
    }
    is StealingRound -> {
      val stealingRoundProps = StealingRoundProps(
        allPlayers = renderState.allPlayers,
        playerPool = renderState.playerPool,
        roundNumber = renderState.round,
        startingPlayer = renderState.startingPlayer,
        gifts = renderState.gifts,
        settings = renderState.settings,
      )
      BodyAndModalsScreen(
        body = context.renderChild(stealingRoundWorkflow, stealingRoundProps) { output ->
          action {
            state = when (output) {
              is StealingRoundOutput.ChangeGameSettings -> {
                ChangeGameSettings(
                  allPlayers = renderState.allPlayers,
                  playerPool = renderState.playerPool,
                  round = renderState.round,
                  player = output.currentPlayer,
                  gifts = output.gifts,
                  settings = renderState.settings,
                )
              }
              is StealingRoundOutput.EndRound -> {
                OpeningGift(
                  allPlayers = renderState.allPlayers,
                  playerPool = renderState.playerPool,
                  round = renderState.round,
                  player = output.playerOpeningGift,
                  gifts = output.gifts,
                  settings = renderState.settings,
                )
              }
            }
          }
        }
      )
    }
    is OpeningGift -> {
      val openGiftProps = OpenGiftProps(
        player = renderState.player,
        round = renderState.round,
      )
      BodyAndModalsScreen(
        body = context.renderChild(openGiftWorkflow, openGiftProps) { gift ->
          action {
            val newGifts = renderState.gifts.clearOwnerHistory() + renderState.player.owns(gift)
            state = if (renderState.playerPool.isNotEmpty()) {
              PickingPlayer(
                allPlayers = renderState.allPlayers,
                playerPool = renderState.playerPool,
                round = renderState.round + 1,
                gifts = newGifts,
                settings = renderState.settings,
              )
            } else {
              EndGame(gifts = newGifts)
            }
          }
        }
      )
    }
    is EndGame -> {
      val endGameProps = EndGameProps(finalGifts = renderState.gifts.clearOwnerHistory())
      BodyAndModalsScreen(
        body = context.renderChild(endGameWorkflow, endGameProps) {
          action {
            setOutput(Unit)
          }
        }
      )
    }
    is ChangeGameSettings -> {
      val changeableSettings = ChangeableSettings(
        settings = renderState.settings,
        gifts = renderState.gifts,
      )
      BodyAndModalsScreen(
        body = context.renderChild(gameSettingsWorkflow, changeableSettings) { output ->
          action {
            state = StealingRound(
              allPlayers = renderState.allPlayers,
              playerPool = renderState.playerPool,
              round = renderState.round,
              startingPlayer = renderState.player,
              gifts = output.gifts,
              settings = output.settings,
            )
          }
        }
      )
    }
  }

  override fun snapshotState(state: AppState): Snapshot {
    return AppState.serializer().toSnapshot(state)
  }
}
