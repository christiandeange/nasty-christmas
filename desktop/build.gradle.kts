import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("org.jetbrains.compose")
}

dependencies {
  implementation(compose.desktop.currentOs)

  implementation(project(":app"))
  implementation(project(":store"))
}

compose.desktop {
  application {
    mainClass = "com.deange.nastychristmas.MainKt"
  }
}
