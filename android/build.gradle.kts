plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("plugin.serialization")
}

android {
  namespace = "com.deange.nastychristmas"
  compileSdk = 33

  defaultConfig {
    applicationId = "com.deange.nastychristmas"
    minSdk = 26
    targetSdk = 33
    versionCode = 1
    versionName = "1.0.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
    resValues = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.get()
  }
}

kotlin {
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
