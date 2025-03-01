plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.kotlin.compose)
  alias(plugins.kotlin.multiplatform)
  alias(plugins.kotlin.serialization)
  alias(plugins.android.library)
  alias(plugins.jetbrains.compose)
}

android {
  namespace = "com.deange.nastychristmas.core"
  compileSdk = 35

  buildFeatures {
    compose = true
    resValues = true
  }

  sourceSets.named("main").configure {
    res.srcDir("resources")
    assets.srcDir("fonts")
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
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
    all {
      languageSettings.apply {
        optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        optIn("kotlin.RequiresOptIn")
      }
    }

    val commonMain by getting {
      dependencies {
        api(project(":firebase"))
        api(project(":store"))

        api(compose.components.resources)
        api(compose.foundation)
        api(compose.material3)
        api(compose.runtime)
        api(compose.ui)

        api(libs.kotlinx.datetime)
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
        implementation(compose.html.core)

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
