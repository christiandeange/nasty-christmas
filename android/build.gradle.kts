plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.android.application)
  alias(plugins.kotlin.android)
  alias(plugins.kotlin.serialization)
}

android {
  namespace = "com.deange.nastychristmas"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.deange.nastychristmas"
    minSdk = 26
    targetSdk = 34
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
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
