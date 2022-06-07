package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Gift
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
class OpenGiftWorkflow : StatefulWorkflow<OpenGiftProps, OpenGiftState, Gift, ViewRendering>() {
  override fun initialState(props: OpenGiftProps, snapshot: Snapshot?): OpenGiftState {
    return OpenGiftState.serializer().fromSnapshot(snapshot)
      ?: OpenGiftState(TextController(""))
  }

  override fun render(
    renderProps: OpenGiftProps,
    renderState: OpenGiftState,
    context: RenderContext
  ): ViewRendering {
    return OpenGiftScreen(
      playerName = renderProps.player.name,
      roundNumber = renderProps.round,
      giftName = renderState.giftName,
      onAddGift = context.eventHandler { giftName ->
        setOutput(Gift(giftName.trim()))
      }
    )
  }

  override fun snapshotState(state: OpenGiftState): Snapshot {
    return OpenGiftState.serializer().toSnapshot(state)
  }
}
