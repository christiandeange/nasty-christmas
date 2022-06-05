package com.deange.nastychristmas.ui.workflow

data class BottomSheetContainerScreen<B : Any, T : BottomSheetScreen>(
  val beneathModals: B,
  val modals: List<T> = emptyList()
) {
  constructor(
    baseScreen: B,
    bottomSheet: T
  ) : this(baseScreen, listOf(bottomSheet))

  constructor(
    baseScreen: B,
    vararg bottomSheets: T
  ) : this(baseScreen, bottomSheets.toList())
}
