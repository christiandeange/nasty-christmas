package com.deange.nastychristmas.registry

import com.deange.nastychristmas.workflow.AppState

data class RegistryState(
  val appState: AppState,
  val gameCode: GameCodeStatus,
) {
  sealed interface GameCodeStatus {
    data object None : GameCodeStatus
    data class Unvalidated(val code: String) : GameCodeStatus
    data class Validated(val code: String) : GameCodeStatus
  }
}
