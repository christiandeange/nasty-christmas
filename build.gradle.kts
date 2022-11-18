import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.android.application") version "8.0.0-alpha08" apply false
  id("com.android.library") version "8.0.0-alpha08" apply false
  id("dev.icerock.mobile.multiplatform-resources") version "0.20.1" apply false
  id("org.jetbrains.compose") version "1.2.0" apply false
  kotlin("multiplatform") version "1.7.10" apply false
  kotlin("android") version "1.7.10" apply false
  kotlin("plugin.serialization") version "1.7.10" apply false
}

allprojects {
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
  }
  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "11"
    targetCompatibility = "11"
  }
}
