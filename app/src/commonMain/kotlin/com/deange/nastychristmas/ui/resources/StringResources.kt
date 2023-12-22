package com.deange.nastychristmas.ui.resources

class StringResources(
  add: String,
  appName: String,
  autoScrollSpeedHint: String,
  back: String,
  confirm: String,
  confirmResetApp: String,
  editGiftNames: String,
  errorGiftExists: String,
  enforceOwnershipDescription: String,
  enforceOwnershipTitle: String,
  giftHint: String,
  hideUnstealableGifts: String,
  letsGetNasty: String,
  newGame: String,
  notApplicable: String,
  ok: String,
  openGiftDescription: String,
  openGiftRoundTitle: String,
  openGiftTitle: String,
  playerHint: String,
  remove: String,
  removePlayer: String,
  resetApp: String,
  round: String,
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
  val appName = StringResource(appName)
  val autoScrollSpeedHint = StringResource(autoScrollSpeedHint)
  val back = StringResource(back)
  val confirm = StringResource(confirm)
  val confirmResetApp = StringResource(confirmResetApp)
  val editGiftNames = StringResource(editGiftNames)
  val errorGiftExists = StringResource(errorGiftExists)
  val enforceOwnershipDescription = StringResource(enforceOwnershipDescription)
  val enforceOwnershipTitle = StringResource(enforceOwnershipTitle)
  val giftHint = StringResource(giftHint)
  val hideUnstealableGifts = StringResource(hideUnstealableGifts)
  val letsGetNasty = StringResource(letsGetNasty)
  val newGame = StringResource(newGame)
  val notApplicable = StringResource(notApplicable)
  val ok = StringResource(ok)
  val openGiftDescription = StringResource(openGiftDescription)
  val openGiftRoundTitle = StringResource(openGiftRoundTitle)
  val openGiftTitle = StringResource(openGiftTitle)
  val playerHint = StringResource(playerHint)
  val remove = StringResource(remove)
  val removePlayer = StringResource(removePlayer)
  val resetApp = StringResource(resetApp)
  val round = StringResource(round)
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
