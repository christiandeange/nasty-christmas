plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.android.application) apply false
  alias(plugins.android.library) apply false
  alias(plugins.jetbrains.compose) apply false
  alias(plugins.kotlin.android) apply false
  alias(plugins.kotlin.jvm) apply false
  alias(plugins.kotlin.multiplatform) apply false
  alias(plugins.kotlin.serialization) apply false
}
