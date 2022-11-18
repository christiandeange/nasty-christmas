plugins {
  id("com.android.library")
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

android {
  namespace = "com.deange.nastychristmas.store"
  compileSdk = 33
}

kotlin {
  jvm()
  android()
  js(IR) {
    browser()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(libs.kotlinx.coroutines)
        api(libs.kotlinx.serialization.json)
        api(libs.kotlinx.serialization.cbor)
      }
    }
    val jvmMain by getting {
      dependencies {
        api(libs.androidx.datastore.core)
      }
    }
    val androidMain by getting {
      dependsOn(jvmMain)
    }
  }
}
