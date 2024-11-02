import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import java.util.Properties

plugins {
  @Suppress("DSL_SCOPE_VIOLATION") val plugins = libs.plugins

  alias(plugins.buildkonfig)
  alias(plugins.kotlin.compose)
  alias(plugins.kotlin.multiplatform)
  alias(plugins.kotlin.serialization)
  alias(plugins.jetbrains.compose)
}

kotlin {
  js(IR) {
    browser()
    binaries.executable()
  }

  jvmToolchain(17)

  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(compose.html.core)

        implementation(project(":app"))
        implementation(project(":store"))

        implementation(npm("buffer", "6.0.3"))
        implementation(npm("process", "0.11.10"))
        implementation(npm("url", "0.11.4"))
      }

      resources.srcDir("../app/resources")
      resources.srcDir("../app/fonts")
    }
  }

  targets.withType<KotlinJsIrTarget>().configureEach {
    val main by compilations.getting {
      tasks.named(processResourcesTaskName).configure {
        dependsOn(tasks.named("unpackSkikoWasmRuntime"))
      }
    }
  }
}

buildkonfig {
  packageName = "com.deange.nastychristmas.firebase"

  defaultConfigs {
    val properties = Properties()
    properties.load(file("firebase-credentials.properties").reader())
    properties.entries.forEach { entry ->
      buildConfigField(STRING, entry.key.toString(), entry.value.toString())
    }
  }
}

val buildDir = project.layout.buildDirectory.asFile.get()

val outputDir = buildDir.resolve("libs")
val distributions = buildDir.resolve("dist/js/productionExecutable")
val jsAppSrc = project.projectDir.resolve("jsapp")
val jsAppBuild = buildDir.resolve("jsapp")
val packageJson = jsAppBuild.resolve("package.json")
val packageLockJson = jsAppBuild.resolve("package-lock.json")
val nodeModules = jsAppBuild.resolve("node_modules")

val copyJsAppFilesTask = tasks.register<Copy>("copyJsAppFiles") {
  from(jsAppSrc)
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

val copyJsAppDistTask = tasks.register<Copy>("copyJsAppDist") {
  dependsOn(tasks.named("jsBrowserDistribution"))
  dependsOn(npmInstallTask)

  from(distributions)
  into(jsAppBuild.resolve("dist"))
}

val copyJsAppIndexHtmlTask = tasks.register<Copy>("copyJsAppIndexHtml") {
  dependsOn(tasks.named("jsBrowserDistribution"))
  dependsOn(npmInstallTask)

  from(distributions.resolve("index.html"))
  into(jsAppBuild)
}

val jsDistribution = tasks.register<Zip>("jsDistribution") {
  dependsOn(npmInstallTask)
  dependsOn(copyJsAppDistTask)
  dependsOn(copyJsAppIndexHtmlTask)

  from(jsAppBuild)
  destinationDirectory.set(outputDir)
}

tasks.register<Exec>("jsDeploy") {
  dependsOn(jsDistribution)
  workingDir(jsAppBuild)
  commandLine("gcloud", "app", "deploy")
}
