plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.android.library)
  alias(plugins.kotlin.multiplatform)
  alias(plugins.kotlin.serialization)
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
