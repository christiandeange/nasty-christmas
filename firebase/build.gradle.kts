plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.deange.nastychristmas.firebase"
  compileSdk = 36
}

kotlin {
  androidTarget()
  jvm()
  js(IR) {
    browser()
    binaries.executable()
  }

  jvmToolchain(17)

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(libs.kotlinx.coroutines)
        api(libs.kotlinx.serialization.json)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.firebase.cloud.firestore)
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(libs.firebase.cloud.firestore)
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(libs.firebase.admin)
        implementation(libs.moshi)
      }
    }
  }
}
