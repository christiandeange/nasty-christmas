package com.deange.nastychristmas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.lifecycle.lifecycleScope
import com.deange.nastychristmas.ui.theme.NastyChristmasTheme
import com.deange.nastychristmas.ui.workflow.BottomSheets
import com.deange.nastychristmas.ui.workflow.ViewEnvironment
import com.deange.nastychristmas.workflow.AppScreen
import com.deange.nastychristmas.workflow.AppWorkflow
import com.deange.nastychristmas.workflow.AppViewModel
import com.deange.nastychristmas.workflow.AppViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val workflow = AppWorkflow()

  private val viewModel: AppViewModel by viewModels {
    AppViewModelFactory(this, workflow, intent.extras)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val environment = ViewEnvironment()

    setContent {
      NastyChristmasTheme {
        val maybeRendering by produceState<AppScreen?>(null, viewModel) {
          viewModel.renderings.collect { value = it }
        }

        maybeRendering?.let { rendering ->
          val bottomSheets = rendering.modals
          val screen = rendering.beneathModals

          BottomSheets(bottomSheets, environment) {
            screen.View(environment)
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
