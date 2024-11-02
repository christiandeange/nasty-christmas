package com.deange.nastychristmas.firebase

import android.content.Context
import android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import dev.gitlive.firebase.Firebase as MultiplatformFirebase

fun initializeFirebase(context: Context): Firebase {
  MultiplatformFirebase.initialize(context = context)

  val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
  val isDebuggable = packageInfo.applicationInfo!!.flags and FLAG_DEBUGGABLE != 0

  MultiplatformFirebase.firestore.setLoggingEnabled(isDebuggable)

  return Firebase(
    firestore = AndroidFirestore(),
  )
}
