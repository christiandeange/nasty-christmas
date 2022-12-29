import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.kotlin.multiplatform)
  alias(plugins.kotlin.serialization)
  alias(plugins.jetbrains.compose)
}

kotlin {
  js(IR) {
    browser()
    binaries.executable()
  }

  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(compose.web.core)

        implementation(project(":app"))
        implementation(project(":store"))

        implementation(npm("process", "0.11.10"))
        implementation(npm("url", "0.11.0"))
      }

      resources.srcDir("../app/resources")
      resources.srcDir("../app/fonts")
    }
  }

  targets.withType<KotlinJsIrTarget>().configureEach {
    val main by compilations.getting

    tasks.named(main.processResourcesTaskName).configure {
      dependsOn(tasks.named("unpackSkikoWasmRuntime${targetName.capitalize()}"))
    }
  }
}

tasks.withType<KotlinWebpack>().configureEach {
  devServer?.port = 3000
}

afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
    nodeVersion = "16.0.0"
  }
}

compose.experimental {
  web.application {}
}

val outputDir = project.buildDir.resolve("libs")
val distributions = project.buildDir.resolve("distributions")
val jsAppSrc = project.projectDir.resolve("jsapp")
val jsAppBuild = project.buildDir.resolve("jsapp")
val packageJson = jsAppBuild.resolve("package.json")
val packageLockJson = jsAppBuild.resolve("package-lock.json")
val nodeModules = jsAppBuild.resolve("node_modules")

val copyJsAppFilesTask = tasks.register<Copy>("copyJsAppFiles") {
  from(jsAppSrc)
  into(jsAppBuild)
}

val copyJsAppDistTask = tasks.register<Copy>("copyJsAppDist") {
  dependsOn(tasks.named("jsBrowserDistribution"))

  from(distributions)
  into(jsAppBuild.resolve("dist"))
}

val copyJsAppIndexHtmlTask = tasks.register<Copy>("copyJsAppIndexHtml") {
  dependsOn(tasks.named("jsBrowserDistribution"))

  from(distributions.resolve("index.html"))
  into(jsAppBuild)
}

val npmInstallTask = tasks.register<Exec>("npmInstall") {
  dependsOn(copyJsAppFilesTask)
  inputs.dir(jsAppBuild)
  outputs.dir(nodeModules)
  outputs.file(packageLockJson)

  commandLine("npm", "install")
  workingDir(jsAppBuild)
}

tasks.register<Zip>("jsDistribution") {
  dependsOn(npmInstallTask)
  dependsOn(copyJsAppDistTask)
  dependsOn(copyJsAppIndexHtmlTask)

  from(jsAppBuild)
  destinationDirectory.set(outputDir)
}
