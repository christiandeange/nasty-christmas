import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.kotlin.multiplatform)
  alias(plugins.kotlin.serialization)
  alias(plugins.android.library)
  alias(plugins.jetbrains.compose)
}

android {
  namespace = "com.deange.nastychristmas.core"
  compileSdk = 33

  buildFeatures {
    compose = true
    resValues = true
  }

  sourceSets.named("main").configure {
    res.srcDir("resources")
    assets.srcDir("fonts")
  }
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

        implementation(npm("@messageformat/core", "3.0.0"))
      }

      resources.srcDir("resources")
      resources.srcDir("fonts")
    }
    val jvmMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
      }

      resources.srcDir("resources")
      resources.srcDir("fonts")
    }
  }
}
