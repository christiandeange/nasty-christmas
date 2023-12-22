package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.end.EndGameProps
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.init.PlayersOutput
import com.deange.nastychristmas.init.PlayersOutput.NoPlayers
import com.deange.nastychristmas.init.PlayersOutput.StartWithPlayers
import com.deange.nastychristmas.init.PlayersProps
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.owns
import com.deange.nastychristmas.round.NewRoundOutput.PlayerSelected
import com.deange.nastychristmas.round.NewRoundOutput.UpdateGameStateWithPlayer
import com.deange.nastychristmas.round.NewRoundProps
import com.deange.nastychristmas.round.NewRoundWorkflow
import com.deange.nastychristmas.round.OpenGiftProps
import com.deange.nastychristmas.round.OpenGiftWorkflow
import com.deange.nastychristmas.round.StealingRoundOutput
import com.deange.nastychristmas.round.StealingRoundProps
import com.deange.nastychristmas.round.StealingRoundWorkflow
import com.deange.nastychristmas.settings.ChangeableSettings
import com.deange.nastychristmas.settings.GameSettings
import com.deange.nastychristmas.settings.GameSettingsOutput.ResetGame
import com.deange.nastychristmas.settings.GameSettingsOutput.UpdateGameSettings
import com.deange.nastychristmas.settings.GameSettingsWorkflow
import com.deange.nastychristmas.state.GameState
import com.deange.nastychristmas.state.asGameState
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.deange.nastychristmas.workflow.AppOutput.ClearGameState
import com.deange.nastychristmas.workflow.AppOutput.Exit
import com.deange.nastychristmas.workflow.AppOutput.SaveGameState
import com.deange.nastychristmas.workflow.AppProps.NewGame
import com.deange.nastychristmas.workflow.AppProps.RestoredFromSave
import com.deange.nastychristmas.workflow.AppState.ChangeGameSettings
import com.deange.nastychristmas.workflow.AppState.EndGame
import com.deange.nastychristmas.workflow.AppState.InitializingPlayers
import com.deange.nastychristmas.workflow.AppState.OpeningGift
import com.deange.nastychristmas.workflow.AppState.PickingPlayer
import com.deange.nastychristmas.workflow.AppState.StealingRound
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action

class AppWorkflow(
  private val playersWorkflow: PlayersWorkflow,
  private val newRoundWorkflow: NewRoundWorkflow,
  private val openGiftWorkflow: OpenGiftWorkflow,
  private val stealingRoundWorkflow: StealingRoundWorkflow,
  private val endGameWorkflow: EndGameWorkflow,
  private val gameSettingsWorkflow: GameSettingsWorkflow,
) : StatefulWorkflow<AppProps, AppState, AppOutput, ViewRendering>() {
  override fun initialState(props: AppProps, snapshot: Snapshot?): AppState {
    return AppState.serializer().fromSnapshot(snapshot)
      ?: when (props) {
        is NewGame -> InitializingPlayers(allPlayers = emptyList())
        is RestoredFromSave -> stateFromRestoredGameState(props.gameState)
      }
  }

  override fun render(
    renderProps: AppProps,
    renderState: AppState,
    context: RenderContext
  ): ViewRendering = when (renderState) {
    is InitializingPlayers -> {
      context.renderInitializingPlayers(renderState)
    }
    is PickingPlayer -> {
      context.renderPickingPlayer(renderState)
    }
    is StealingRound -> {
      context.renderStealingRound(renderState)
    }
    is OpeningGift -> {
      context.renderOpeningGift(renderState)
    }
    is EndGame -> {
      context.renderEndGame(renderState)
    }
    is ChangeGameSettings -> {
      context.renderStealingRound(
        StealingRound(
          allPlayers = renderState.allPlayers,
          playerPool = renderState.playerPool,
          startingPlayer = renderState.player,
          round = renderState.round,
          gifts = renderState.gifts,
          stats = renderState.stats,
          settings = renderState.settings,
        )
      )
      context.renderChangeGameSettings(renderState)
    }
  }

  override fun snapshotState(state: AppState): Snapshot {
    return AppState.serializer().toSnapshot(state)
  }

  private fun stateFromRestoredGameState(gameState: GameState): AppState {
    return if (gameState.roundNumber < 0) {
      InitializingPlayers(
        allPlayers = gameState.allPlayers,
      )
    } else if (gameState.playerPool.isEmpty()) {
      EndGame(
        stats = gameState.stats,
        gifts = gameState.gifts,
      )
    } else if (gameState.currentPlayer == null) {
      PickingPlayer(
        allPlayers = gameState.allPlayers,
        playerPool = gameState.playerPool,
        selectedPlayer = null,
        round = gameState.roundNumber,
        gifts = gameState.gifts,
        stats = gameState.stats,
        settings = gameState.settings,
      )
    } else if (gameState.gifts.stealableGifts(gameState.currentPlayer).isEmpty()) {
      OpeningGift(
        allPlayers = gameState.allPlayers,
        playerPool = gameState.playerPool,
        round = gameState.roundNumber,
        player = gameState.currentPlayer,
        gifts = gameState.gifts,
        stats = gameState.stats,
        settings = gameState.settings,
      )
    } else {
      StealingRound(
        allPlayers = gameState.allPlayers,
        playerPool = gameState.playerPool,
        round = gameState.roundNumber,
        startingPlayer = gameState.currentPlayer,
        gifts = gameState.gifts,
        stats = gameState.stats,
        settings = gameState.settings,
      )
    }
  }

  private fun RenderContext.renderInitializingPlayers(
    renderState: InitializingPlayers,
  ): ViewRendering {
    val playersProps = PlayersProps(players = renderState.allPlayers)
    return renderChild(playersWorkflow, playersProps) { output ->
      savingGameStateAction {
        when (output) {
          is NoPlayers -> setOutput(Exit)
          is PlayersOutput.PlayersUpdated -> {
            state = InitializingPlayers(allPlayers = output.players)
          }
          is StartWithPlayers -> {
            state = PickingPlayer(
              allPlayers = output.players,
              playerPool = output.players.toSet(),
              selectedPlayer = null,
              round = 1,
              gifts = GiftOwners(emptyMap()),
              stats = GameStats(),
              settings = GameSettings.Default,
            )
          }
        }
      }
    }
  }

  private fun RenderContext.renderPickingPlayer(
    renderState: PickingPlayer
  ): ViewRendering {
    val newRoundProps = NewRoundProps(
      allPlayers = renderState.allPlayers,
      playerPool = renderState.playerPool,
      selectedPlayer = renderState.selectedPlayer,
      roundNumber = renderState.round,
    )
    return renderChild(newRoundWorkflow, newRoundProps) { output ->
      savingGameStateAction {
        when (output) {
          is UpdateGameStateWithPlayer -> {
            val player = output.player
            state = renderState.copy(
              playerPool = renderState.playerPool - player,
              selectedPlayer = player,
            )
          }
          is PlayerSelected -> {
            val player = output.player
            state = if (renderState.gifts.stealableGifts(player).isEmpty()) {
              OpeningGift(
                allPlayers = renderState.allPlayers,
                playerPool = renderState.playerPool - player,
                round = renderState.round,
                player = player,
                gifts = renderState.gifts,
                stats = renderState.stats + GameStats(opensByPlayer = mapOf(player to 1)),
                settings = renderState.settings,
              )
            } else {
              StealingRound(
                allPlayers = renderState.allPlayers,
                playerPool = renderState.playerPool - player,
                round = renderState.round,
                startingPlayer = player,
                gifts = renderState.gifts,
                stats = renderState.stats,
                settings = renderState.settings,
              )
            }
          }
        }
      }
    }
  }

  private fun RenderContext.renderStealingRound(
    renderState: StealingRound,
  ): ViewRendering {
    val stealingRoundProps = StealingRoundProps(
      allPlayers = renderState.allPlayers,
      playerPool = renderState.playerPool,
      roundNumber = renderState.round,
      startingPlayer = renderState.startingPlayer,
      gifts = renderState.gifts,
      settings = renderState.settings,
    )
    return renderChild(stealingRoundWorkflow, stealingRoundProps) { output ->
      savingGameStateAction {
        state = when (output) {
          is StealingRoundOutput.ChangeGameSettings -> {
            ChangeGameSettings(
              allPlayers = renderState.allPlayers,
              playerPool = renderState.playerPool,
              round = renderState.round,
              player = output.currentPlayer,
              gifts = output.gifts,
              stats = renderState.stats + output.stats,
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
              stats = renderState.stats + output.stats,
              settings = renderState.settings,
            )
          }
          is StealingRoundOutput.UpdateGameSettings -> {
            renderState.copy(settings = output.gameSettings)
          }
        }
      }
    }
  }

  private fun RenderContext.renderOpeningGift(
    renderState: OpeningGift,
  ): ViewRendering {
    val openGiftProps = OpenGiftProps(
      player = renderState.player,
      round = renderState.round,
      giftNames = renderState.gifts.map { it.value.gift.name }.toSet(),
    )
    return renderChild(openGiftWorkflow, openGiftProps) { gift ->
      savingGameStateAction {
        val newGifts = renderState.gifts.clearOwnerHistory() + renderState.player.owns(gift)
        state = if (renderState.playerPool.isNotEmpty()) {
          PickingPlayer(
            allPlayers = renderState.allPlayers,
            playerPool = renderState.playerPool,
            selectedPlayer = null,
            round = renderState.round + 1,
            gifts = newGifts,
            stats = renderState.stats,
            settings = renderState.settings,
          )
        } else {
          setOutput(ClearGameState)
          EndGame(
            gifts = newGifts,
            stats = renderState.stats,
          )
        }
      }
    }
  }

  private fun RenderContext.renderEndGame(
    renderState: EndGame,
  ): ViewRendering {
    val endGameProps = EndGameProps(
      finalGifts = renderState.gifts.clearOwnerHistory(),
      stats = renderState.stats,
    )
    return renderChild(endGameWorkflow, endGameProps) {
      savingGameStateAction {
        state = InitializingPlayers(allPlayers = emptyList())
      }
    }
  }

  private fun RenderContext.renderChangeGameSettings(
    renderState: ChangeGameSettings
  ): ViewRendering {
    val changeableSettings = ChangeableSettings(
      settings = renderState.settings,
      gifts = renderState.gifts,
      stats = renderState.stats,
    )
    return renderChild(gameSettingsWorkflow, changeableSettings) { output ->
      savingGameStateAction {
        when (output) {
          is UpdateGameSettings -> {
            state = StealingRound(
              allPlayers = renderState.allPlayers,
              playerPool = renderState.playerPool,
              round = renderState.round,
              startingPlayer = renderState.player,
              gifts = output.settings.gifts,
              stats = output.settings.stats,
              settings = output.settings.settings,
            )
          }
          is ResetGame -> {
            state = initialState(NewGame, snapshot = null)
            setOutput(ClearGameState)
          }
        }
      }
    }
  }

  private fun savingGameStateAction(
    name: String = "",
    update: WorkflowAction<AppProps, AppState, AppOutput>.Updater.() -> Unit,
  ) = action(name) {
    update()

    val savedState: GameState = state.asGameState()
    setOutput(SaveGameState(savedState))
  }
}
