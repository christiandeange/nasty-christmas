package com.deange.nastychristmas.firebase

data class FirebaseConfiguration(
  val applicationId: String,
  val apiKey: String,
  val projectId: String? = null,
  val authDomain: String? = null,
)
