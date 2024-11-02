package com.deange.nastychristmas.registry

import com.deange.nastychristmas.firebase.CurrentStateSnapshot
import com.deange.nastychristmas.firebase.Firestore
import com.deange.nastychristmas.firebase.GiftSnapshot
import com.deange.nastychristmas.model.Gift
import com.deange.nastychristmas.model.OwnedGift
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.round.StealOrOpenChoice
import com.deange.nastychristmas.round.StealingRoundScreen
import com.deange.nastychristmas.store.PersistentStorage
import com.deange.nastychristmas.store.preference
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.runningWorker

class RegistryWorkflow(
  storage: PersistentStorage,
  firestore: Firestore,
) : StatefulWorkflow<Unit, RegistryState, Unit, ViewRendering>() {
  private var showUnstealableGifts by storage.preference("showUnstealableGifts", false)
  private var autoScrollSpeed by storage.preference("autoScrollSpeed", 1)

  private val gameStateWorker = DocumentWorker(firestore, "current-state/default/", CurrentStateSnapshot::class)
  private val giftsWorker = CollectionWorker(firestore, "gifts/", GiftSnapshot::class)

  override fun initialState(
    props: Unit,
    snapshot: Snapshot?,
  ): RegistryState {
    return RegistryState.serializer().fromSnapshot(snapshot)
      ?: RegistryState(
        currentStateData = null,
        gifts = emptyMap(),
        showUnstealableGifts = showUnstealableGifts,
        autoScrollSpeed = autoScrollSpeed,
      )
  }

  override fun render(
    renderProps: Unit,
    renderState: RegistryState,
    context: RenderContext,
  ): ViewRendering {
    context.runningWorker(gameStateWorker) { currentStateData ->
      action {
        state = state.copy(currentStateData = currentStateData)
      }
    }

    context.runningWorker(giftsWorker) { gifts ->
      action {
        state = state.copy(
          gifts = gifts.associate { gift ->
            Player(gift.owner) to OwnedGift(
              gift = Gift(gift.gift),
              owner = Player(gift.owner),
              owners = gift.owners.map { Player(it) }.toSet(),
            )
          },
        )
      }
    }

    val choices = buildList {
      add(
        StealOrOpenChoice.Open(
          isSelected = false,
          onPicked = { },
        )
      )

      addAll(
        renderState.gifts.map { (player, gift) ->
          StealOrOpenChoice.Steal(
            playerName = player.name,
            giftName = gift.gift.name,
            isSelected = false,
            isEnabled = renderState.currentStateData?.player?.let(::Player) !in gift.owners,
            onPicked = { },
          )
        }.sortedBy { !it.isEnabled }
      )
    }

    return StealingRoundScreen(
      playerName = renderState.currentStateData?.player ?: "Someone",
      roundNumber = renderState.currentStateData?.round ?: 0,
      choices = choices,
      showUnstealableGifts = renderState.showUnstealableGifts,
      autoScrollSpeed = renderState.autoScrollSpeed,
      readOnly = true,
      onChangeSettings = {},
      onUndo = null,
      onConfirmChoice = {},
      onChangeShowUnstealableGifts = context.eventHandler { showUnstealableGifts ->
        this@RegistryWorkflow.showUnstealableGifts = showUnstealableGifts
        state = state.copy(showUnstealableGifts = showUnstealableGifts)
      },
      onChangeAutoScrollSpeed = context.eventHandler { autoScrollSpeed ->
        this@RegistryWorkflow.autoScrollSpeed = autoScrollSpeed
        state = state.copy(autoScrollSpeed = autoScrollSpeed)
      }
    )
  }

  override fun snapshotState(state: RegistryState): Snapshot {
    return RegistryState.serializer().toSnapshot(state)
  }
}
