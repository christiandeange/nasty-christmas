plugins {
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.jetbrains.compose)
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
