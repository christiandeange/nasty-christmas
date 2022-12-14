package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Gift
import com.deange.nastychristmas.ui.compose.TextController
import com.deange.nastychristmas.ui.theme.Strings
import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.deange.nastychristmas.ui.workflow.fromSnapshot
import com.deange.nastychristmas.ui.workflow.toSnapshot
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

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
      errorGiftExists = renderState.giftName.textValue in renderProps.giftNames,
      onAddGift = context.eventHandler { name ->
        val giftName = name.trim()
        if (giftName !in props.giftNames) {
          setOutput(Gift(giftName))
        }
      }
    )
  }

  override fun snapshotState(state: OpenGiftState): Snapshot {
    return OpenGiftState.serializer().toSnapshot(state)
  }
}
