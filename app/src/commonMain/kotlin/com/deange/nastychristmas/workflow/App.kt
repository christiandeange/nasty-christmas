package com.deange.nastychristmas.workflow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deange.nastychristmas.ui.resources.EN
import com.deange.nastychristmas.ui.resources.StringResources
import com.deange.nastychristmas.ui.theme.Language
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.workflow.WorkflowRendering

@Composable
fun App(
  workflow: AppWorkflow,
  props: AppProps,
  onOutput: (AppOutput) -> Unit,
) {
  NastyChristmasTheme {
    Language(StringResources.EN) {
      WorkflowRendering(
        workflow = workflow,
        props = props,
        onOutput = onOutput,
      ) { screen ->
        Surface(Modifier.fillMaxSize()) {
          Box {
            screen.Content()
          }
        }
      }
    }
  }
}
