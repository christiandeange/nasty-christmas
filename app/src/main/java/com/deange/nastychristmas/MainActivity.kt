package com.deange.nastychristmas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.deange.nastychristmas.init.PlayersWorkflow
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.theme.StatusBarTheme
import com.deange.nastychristmas.ui.workflow.BottomSheets
import com.deange.nastychristmas.ui.workflow.WorkflowRenderings
import com.deange.nastychristmas.workflow.AppViewModel
import com.deange.nastychristmas.workflow.AppViewModelFactory
import com.deange.nastychristmas.workflow.AppWorkflow
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import kotlinx.coroutines.launch

@OptIn(WorkflowUiExperimentalApi::class)
class MainActivity : ComponentActivity() {

  private val workflow by lazy {
    AppWorkflow(
      playersWorkflow = PlayersWorkflow(),
    )
  }

  private val viewModel: AppViewModel by viewModels {
    AppViewModelFactory(this, workflow, intent.extras)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      NastyChristmasTheme {
        StatusBarTheme()
        WorkflowRenderings(viewModel.renderings) { rendering ->
          BottomSheets(rendering.modals) {
            rendering.body.Content()
          }
        }
      }
    }

    lifecycleScope.launch {
      viewModel.waitForExit()
      finish()
    }
  }
}
