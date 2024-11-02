package com.deange.nastychristmas

import android.app.Application
import com.deange.nastychristmas.firebase.Firebase
import com.deange.nastychristmas.firebase.initializeFirebase

class MainApplication : Application() {
  val firebase: Firebase by lazy {
    initializeFirebase(this)
  }
}
