import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("com.android.library")
  id("dev.icerock.mobile.multiplatform-resources")
  id("org.jetbrains.compose")
}

android {
  namespace = "com.deange.nastychristmas.core"
  compileSdk = 33

  // Remove with AGP 8.
  sourceSets["main"].res.srcDir(
    buildDir
      .resolve("generated")
      .resolve("moko")
      .resolve("androidMain")
      .resolve("res")
  )
}

kotlin {
  jvm()
  android()
  js(IR) {
    browser()
    binaries.executable()
  }

  sourceSets {
    all {
      languageSettings.apply {
        optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        optIn("kotlin.RequiresOptIn")
      }
    }

    val commonMain by getting {
      dependencies {
        api(compose.foundation)
        @OptIn(ExperimentalComposeLibrary::class)
        api(compose.material3)
        api(compose.runtime)
        api(compose.ui)

        api(libs.kotlinx.serialization.json)
        api(libs.mokoresources)
        api(libs.workflow.core)
        api(libs.workflow.runtime)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.androidx.activity.compose)
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(compose.web.core)
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
  }
}

multiplatformResources {
  multiplatformResourcesPackage = "com.deange.nastychristmas.core"
}
