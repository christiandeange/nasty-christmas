package com.deange.nastychristmas.round

object ChoiceSorter : Comparator<StealOrOpenChoice> by compareBy(
  { if (it is StealOrOpenChoice.Open) -1 else 1 },
  { if ((it as StealOrOpenChoice.Steal).isEnabled) -1 else 1 },
  { (it as StealOrOpenChoice.Steal).giftName },
)
