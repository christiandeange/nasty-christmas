package com.deange.nastychristmas.firebase

import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import dev.gitlive.firebase.Firebase as MultiplatformFirebase

fun initializeFirebase(configuration: FirebaseConfiguration): Firebase {
  MultiplatformFirebase.initialize(
    options = FirebaseOptions(
      applicationId = configuration.applicationId,
      apiKey = configuration.apiKey,
      projectId = configuration.projectId,
      authDomain = configuration.authDomain,
    )
  )

  MultiplatformFirebase.firestore.setLoggingEnabled(true)

  return Firebase(
    firestore = JsFirestore(),
  )
}
