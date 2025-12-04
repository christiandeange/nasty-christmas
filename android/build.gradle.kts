plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.google.services)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.deange.nastychristmas"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.deange.nastychristmas"
    minSdk = 26
    targetSdk = 36
    versionCode = 1
    versionName = "1.0.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }

  buildFeatures {
    compose = true
    resValues = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
}

kotlin {
  jvmToolchain(17)

  sourceSets.all {
    languageSettings {
      optIn("kotlin.RequiresOptIn")
      optIn("com.squareup.workflow1.ui.WorkflowUiExperimentalApi")
    }
  }
}

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.core)
  implementation(libs.androidx.datastore.android)
  implementation(libs.androidx.lifecycle)
  implementation(libs.workflow.ui.android)

  implementation(project(":app"))
  implementation(project(":store"))
}
