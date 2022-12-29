plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.kotlin.jvm)
  alias(plugins.kotlin.serialization)
  alias(plugins.jetbrains.compose)
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
