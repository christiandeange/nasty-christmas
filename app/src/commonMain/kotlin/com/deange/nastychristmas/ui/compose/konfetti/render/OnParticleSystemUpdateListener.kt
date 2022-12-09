package com.deange.nastychristmas.ui.compose.konfetti.render

import com.deange.nastychristmas.ui.compose.konfetti.PartySystem

interface OnParticleSystemUpdateListener {
  fun onParticleSystemEnded(system: PartySystem, activeSystems: Int)
}
