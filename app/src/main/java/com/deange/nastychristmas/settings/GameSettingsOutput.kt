package com.deange.nastychristmas.settings

sealed class GameSettingsOutput {
  data class UpdateGameSettings(
    val settings: ChangeableSettings,
  ) : GameSettingsOutput()

  object ResetGame : GameSettingsOutput()
}
