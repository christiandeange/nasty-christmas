package com.deange.nastychristmas.ui.workflow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun BottomSheets(
  sheets: List<BottomSheetScreen>,
  viewEnvironment: ViewEnvironment,
  content: @Composable () -> Unit,
) {
  if (sheets.isNotEmpty()) {
    sheets.forEachIndexed { index, sheet ->
      BottomSheet(sheet, viewEnvironment) {
        if (index == 0) content()
      }
    }
  } else {
    content()
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheet(
  sheet: BottomSheetScreen,
  viewEnvironment: ViewEnvironment,
  content: @Composable () -> Unit,
) {
  val bottomSheetState = rememberModalBottomSheetState(initialValue = Hidden)
  var lastSheet by remember { mutableStateOf(sheet) }

  if (bottomSheetState.currentValue != Hidden) {
    lastSheet = sheet
    DisposableEffect(bottomSheetState) {
      onDispose {
        lastSheet.onCancel()
      }
    }
  }

  LaunchedEffect(bottomSheetState) {
    bottomSheetState.show()
  }

  ModalBottomSheetLayout(
    sheetState = bottomSheetState,
    sheetContent = {
      Box(modifier = Modifier.fillMaxWidth()) {
        sheet.View(viewEnvironment)
      }
    },
    content = content,
  )
}
