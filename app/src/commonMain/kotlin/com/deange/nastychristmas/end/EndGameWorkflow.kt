package com.deange.nastychristmas.end

import com.deange.nastychristmas.ui.workflow.ViewRendering
import com.squareup.workflow1.StatelessWorkflow

class EndGameWorkflow : StatelessWorkflow<EndGameProps, Unit, ViewRendering>() {
  override fun render(
    renderProps: EndGameProps,
    context: RenderContext<EndGameProps, Unit>,
  ): ViewRendering {
    return EndGameScreen(
      stats = renderProps.stats,
      isReadOnly = renderProps.isReadOnly,
      onContinue = context.eventHandler("onContinue") {
        setOutput(Unit)
      }
    )
  }
}
