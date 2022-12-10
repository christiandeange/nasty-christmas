package com.deange.nastychristmas.end

import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.squareup.workflow1.StatelessWorkflow

class EndGameWorkflow : StatelessWorkflow<EndGameProps, Unit, ViewRendering>() {
  override fun render(
    renderProps: EndGameProps,
    context: RenderContext,
  ): ViewRendering {
    return EndGameScreen(
      stats = renderProps.stats,
      onContinue = context.eventHandler {
        setOutput(Unit)
      }
    )
  }
}
