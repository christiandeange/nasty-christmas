package com.deange.nastychristmas.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

fun initializeFirebase(): Firebase {
  val serviceAccount = JvmAdminFirestore::class.java.getResourceAsStream("/service-account-credentials.json")

  FirebaseApp.initializeApp(
    FirebaseOptions.builder()
      .setCredentials(GoogleCredentials.fromStream(serviceAccount))
      .build()
  )

  return Firebase(
    firestore = JvmAdminFirestore(),
  )
}
