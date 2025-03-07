package com.deange.nastychristmas.ui.resources

class StringResources(
  add: String,
  addingPlayers: String,
  appName: String,
  autoScrollSpeedHint: String,
  back: String,
  clear: String,
  confirm: String,
  confirmResetApp: String,
  editGiftNames: String,
  enterGameCode: String,
  errorGiftExists: String,
  enforceOwnershipDescription: String,
  enforceOwnershipTitle: String,
  giftHint: String,
  hideUnstealableGifts: String,
  letsGetNasty: String,
  nastyGiftPromo: String,
  nastyGiftPromoWithGameCode: String,
  newGame: String,
  notApplicable: String,
  ok: String,
  openGiftEndGame: String,
  openGiftEndRound: String,
  openGiftRoundTitle: String,
  openLastGiftTitle: String,
  openNewGiftTitle: String,
  playerHint: String,
  remove: String,
  removePlayer: String,
  resetApp: String,
  round: String,
  roundSelectionTitle: String,
  roundTitle: String,
  settings: String,
  showUnstealableGifts: String,
  statMostOpens: String,
  statMostSteals: String,
  statMostStolenFrom: String,
  statMostStolenGift: String,
  stealFrom: String,
  stealRoundTitle: String,
  thanksForPlaying: String,
  undo: String,
) {
  val add = StringResource(add)
  val addingPlayers = StringResource(addingPlayers)
  val appName = StringResource(appName)
  val autoScrollSpeedHint = StringResource(autoScrollSpeedHint)
  val back = StringResource(back)
  val clear = StringResource(clear)
  val confirm = StringResource(confirm)
  val confirmResetApp = StringResource(confirmResetApp)
  val editGiftNames = StringResource(editGiftNames)
  val enterGameCode = StringResource(enterGameCode)
  val errorGiftExists = StringResource(errorGiftExists)
  val enforceOwnershipDescription = StringResource(enforceOwnershipDescription)
  val enforceOwnershipTitle = StringResource(enforceOwnershipTitle)
  val giftHint = StringResource(giftHint)
  val hideUnstealableGifts = StringResource(hideUnstealableGifts)
  val letsGetNasty = StringResource(letsGetNasty)
  val nastyGiftPromo = StringResource(nastyGiftPromo)
  val nastyGiftPromoWithGameCode = StringResource(nastyGiftPromoWithGameCode)
  val newGame = StringResource(newGame)
  val notApplicable = StringResource(notApplicable)
  val ok = StringResource(ok)
  val openGiftEndGame = StringResource(openGiftEndGame)
  val openGiftDescription = StringResource(openGiftEndRound)
  val openGiftRoundTitle = StringResource(openGiftRoundTitle)
  val openLastGiftTitle = StringResource(openLastGiftTitle)
  val openNewGiftTitle = StringResource(openNewGiftTitle)
  val playerHint = StringResource(playerHint)
  val remove = StringResource(remove)
  val removePlayer = StringResource(removePlayer)
  val resetApp = StringResource(resetApp)
  val round = StringResource(round)
  val roundSelectionTitle = StringResource(roundSelectionTitle)
  val roundTitle = StringResource(roundTitle)
  val settings = StringResource(settings)
  val showUnstealableGifts = StringResource(showUnstealableGifts)
  val statMostOpens = StringResource(statMostOpens)
  val statMostSteals = StringResource(statMostSteals)
  val statMostStolenFrom = StringResource(statMostStolenFrom)
  val statMostStolenGift = StringResource(statMostStolenGift)
  val stealFrom = StringResource(stealFrom)
  val stealRoundTitle = StringResource(stealRoundTitle)
  val thanksForPlaying = StringResource(thanksForPlaying)
  val undo = StringResource(undo)

  companion object
}

expect class StringResource(formatString: String) {
  fun evaluate(vararg formatArgs: Any): String
}
