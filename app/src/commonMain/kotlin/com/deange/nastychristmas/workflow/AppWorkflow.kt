package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.end.EndGameProps
import com.deange.nastychristmas.end.EndGameWorkflow
import com.deange.nastychristmas.init.PlayersOutput.GameCodeUpdated
import com.deange.nastychristmas.init.PlayersOutput.NoPlayers
import com.deange.nastychristmas.init.PlayersOutput.PlayersUpdated
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
import com.deange.nastychristmas.state.GameStateFactory
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
import kotlin.random.Random

class AppWorkflow(
  private val playersWorkflow: PlayersWorkflow,
  private val newRoundWorkflow: NewRoundWorkflow,
  private val openGiftWorkflow: OpenGiftWorkflow,
  private val stealingRoundWorkflow: StealingRoundWorkflow,
  private val endGameWorkflow: EndGameWorkflow,
  private val gameSettingsWorkflow: GameSettingsWorkflow,
  private val gameStateFactory: GameStateFactory,
  private val random: Random,
) : StatefulWorkflow<AppProps, AppState, AppOutput, ViewRendering>() {
  override fun initialState(props: AppProps, snapshot: Snapshot?): AppState {
    return AppState.serializer().fromSnapshot(snapshot)
      ?: when (props) {
        is NewGame -> InitializingPlayers(allPlayers = emptyList(), gameCode = null)
        is RestoredFromSave -> props.gameState.appState
      }
  }

  override fun onPropsChanged(old: AppProps, new: AppProps, state: AppState): AppState {
    return initialState(new, snapshot = null)
  }

  override fun render(
    renderProps: AppProps,
    renderState: AppState,
    context: RenderContext<AppProps, AppState, AppOutput>,
  ): ViewRendering = when (renderState) {
    is InitializingPlayers -> {
      context.renderInitializingPlayers(renderProps, renderState)
    }
    is PickingPlayer -> {
      context.renderPickingPlayer(renderProps, renderState)
    }
    is StealingRound -> {
      context.renderStealingRound(renderProps, renderState)
    }
    is OpeningGift -> {
      context.renderOpeningGift(renderProps, renderState)
    }
    is EndGame -> {
      context.renderEndGame(renderProps, renderState)
    }
    is ChangeGameSettings -> {
      context.renderChangeGameSettings(renderProps, renderState)
    }
  }

  override fun snapshotState(state: AppState): Snapshot {
    return AppState.serializer().toSnapshot(state)
  }

  private fun RenderContext<AppProps, AppState, AppOutput>.renderInitializingPlayers(
    renderProps: AppProps,
    renderState: InitializingPlayers,
  ): ViewRendering {
    val playersProps = PlayersProps(
      players = renderState.allPlayers,
      gameCode = renderState.gameCode,
      isReadOnly = renderProps.isReadOnly,
    )
    return renderChild(playersWorkflow, playersProps) { output ->
      savingGameStateAction {
        when (output) {
          is NoPlayers -> setOutput(Exit)
          is PlayersUpdated -> {
            state = InitializingPlayers(
              allPlayers = output.players,
              gameCode = renderState.gameCode,
            )
          }
          is GameCodeUpdated -> {
            state = InitializingPlayers(
              allPlayers = renderState.allPlayers,
              gameCode = output.gameCode,
            )
          }
          is StartWithPlayers -> {
            state = PickingPlayer(
              allPlayers = output.players,
              playerPool = output.players.toSet(),
              selectedPlayer = null,
              seed = random.nextInt(),
              round = 1,
              gifts = GiftOwners(emptyMap()),
              stats = GameStats(),
              settings = GameSettings.default(gameCode = renderState.gameCode!!),
            )
          }
        }
      }
    }
  }

  private fun RenderContext<AppProps, AppState, AppOutput>.renderPickingPlayer(
    renderProps: AppProps,
    renderState: PickingPlayer
  ): ViewRendering {
    val newRoundProps = NewRoundProps(
      allPlayers = renderState.allPlayers,
      playerPool = renderState.playerPool,
      selectedPlayer = renderState.selectedPlayer,
      seed = renderState.seed,
      roundNumber = renderState.round,
      isReadOnly = renderProps.isReadOnly,
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
                stats = renderState.stats + GameStats(opensByPlayer = mapOf(player.name to 1)),
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
                roundStats = GameStats(),
                settings = renderState.settings,
              )
            }
          }
        }
      }
    }
  }

  private fun RenderContext<AppProps, AppState, AppOutput>.renderStealingRound(
    renderProps: AppProps,
    renderState: StealingRound,
  ): ViewRendering {
    val stealingRoundProps = StealingRoundProps(
      allPlayers = renderState.allPlayers,
      playerPool = renderState.playerPool,
      roundNumber = renderState.round,
      startingPlayer = renderState.startingPlayer,
      gifts = renderState.gifts,
      settings = renderState.settings,
      isReadOnly = renderProps.isReadOnly,
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
              stats = renderState.stats,
              roundStats = renderState.roundStats,
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
          is StealingRoundOutput.UpdateGifts -> {
            renderState.copy(
              startingPlayer = output.currentPlayer,
              gifts = output.gifts,
              roundStats = output.stats,
            )
          }
        }
      }
    }
  }

  private fun RenderContext<AppProps, AppState, AppOutput>.renderOpeningGift(
    renderProps: AppProps,
    renderState: OpeningGift,
  ): ViewRendering {
    val openGiftProps = OpenGiftProps(
      player = renderState.player,
      round = renderState.round,
      giftNames = renderState.gifts.map { it.value.gift.name }.toSet(),
      isReadOnly = renderProps.isReadOnly,
    )
    return renderChild(openGiftWorkflow, openGiftProps) { gift ->
      savingGameStateAction {
        val newGifts = renderState.gifts.clearOwnerHistory() + renderState.player.owns(gift)
        state = if (renderState.playerPool.isNotEmpty()) {
          PickingPlayer(
            allPlayers = renderState.allPlayers,
            playerPool = renderState.playerPool,
            selectedPlayer = null,
            seed = random.nextInt(),
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
            settings = renderState.settings,
          )
        }
      }
    }
  }

  private fun RenderContext<AppProps, AppState, AppOutput>.renderEndGame(
    renderProps: AppProps,
    renderState: EndGame,
  ): ViewRendering {
    val endGameProps = EndGameProps(
      finalGifts = renderState.gifts.clearOwnerHistory(),
      stats = renderState.stats,
      isReadOnly = renderProps.isReadOnly,
    )
    return renderChild(endGameWorkflow, endGameProps) {
      savingGameStateAction {
        state = InitializingPlayers(allPlayers = emptyList(), gameCode = null)
      }
    }
  }

  private fun RenderContext<AppProps, AppState, AppOutput>.renderChangeGameSettings(
    renderProps: AppProps,
    renderState: ChangeGameSettings,
  ): ViewRendering {
    val stealingRoundRendering = renderStealingRound(
      renderProps = renderProps,
      renderState = StealingRound(
        allPlayers = renderState.allPlayers,
        playerPool = renderState.playerPool,
        startingPlayer = renderState.player,
        round = renderState.round,
        gifts = renderState.gifts,
        stats = renderState.stats,
        roundStats = renderState.roundStats,
        settings = renderState.settings,
      ),
    )

    return if (renderProps.isReadOnly) {
      stealingRoundRendering
    } else {
      val changeableSettings = ChangeableSettings(
        settings = renderState.settings,
        gifts = renderState.gifts,
        stats = renderState.stats,
        roundStats = renderState.roundStats,
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
                roundStats = output.settings.roundStats,
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
  }

  private fun savingGameStateAction(
    name: String = "",
    update: WorkflowAction<AppProps, AppState, AppOutput>.Updater.() -> Unit,
  ) = action(name) {
    update()

    setOutput(SaveGameState(gameStateFactory.create(state)))
  }
}
