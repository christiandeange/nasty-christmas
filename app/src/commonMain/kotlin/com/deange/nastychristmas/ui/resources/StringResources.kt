package com.deange.nastychristmas.ui.resources

class StringResources(
  add: String,
  appName: String,
  back: String,
  confirm: String,
  confirmResetApp: String,
  editGiftNames: String,
  enforceOwnershipDescription: String,
  enforceOwnershipTitle: String,
  giftHint: String,
  letsGetNasty: String,
  newGame: String,
  notApplicable: String,
  ok: String,
  openGiftDescription: String,
  openGiftRoundTitle: String,
  openGiftTitle: String,
  playerHint: String,
  remove: String,
  resetApp: String,
  round: String,
  roundTitle: String,
  settings: String,
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
  val back = StringResource(back)
  val confirm = StringResource(confirm)
  val confirmResetApp = StringResource(confirmResetApp)
  val editGiftNames = StringResource(editGiftNames)
  val enforceOwnershipDescription = StringResource(enforceOwnershipDescription)
  val enforceOwnershipTitle = StringResource(enforceOwnershipTitle)
  val giftHint = StringResource(giftHint)
  val letsGetNasty = StringResource(letsGetNasty)
  val newGame = StringResource(newGame)
  val notApplicable = StringResource(notApplicable)
  val ok = StringResource(ok)
  val openGiftDescription = StringResource(openGiftDescription)
  val openGiftRoundTitle = StringResource(openGiftRoundTitle)
  val openGiftTitle = StringResource(openGiftTitle)
  val playerHint = StringResource(playerHint)
  val remove = StringResource(remove)
  val resetApp = StringResource(resetApp)
  val round = StringResource(round)
  val roundTitle = StringResource(roundTitle)
  val settings = StringResource(settings)
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
