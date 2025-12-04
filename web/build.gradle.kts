import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import java.util.Properties

plugins {
  alias(libs.plugins.buildkonfig)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.jetbrains.compose)
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

val projectDir = project.layout.projectDirectory.asFile
val buildDir = project.layout.buildDirectory.asFile.get()

val outputDir = buildDir.resolve("libs")
val distributions = buildDir.resolve("dist/js/productionExecutable")
val jsAppSrc = projectDir.resolve("jsapp")
val jsAppBuild = buildDir.resolve("jsapp")
val packageJson = jsAppBuild.resolve("package.json")
val packageLockJsonApp = jsAppBuild.resolve("package-lock.json")
val nodeModulesApp = jsAppBuild.resolve("node_modules")

val functionsBuild = jsAppBuild.resolve("functions")
val packageJsonFunctions = functionsBuild.resolve("package.json")
val nodeModulesFunctions = functionsBuild.resolve("node_modules")
val packageLockJsonFunctions = functionsBuild.resolve("package-lock.json")

val copyJsAppFilesTask = tasks.register<Copy>("copyJsAppFiles") {
  from(jsAppSrc)
  into(jsAppBuild)
}

val npmInstallAppTask = tasks.register<Exec>("npmInstallApp") {
  dependsOn(copyJsAppFilesTask)

  inputs.file(packageJson)
  outputs.dir(nodeModulesFunctions)
  outputs.file(packageLockJsonApp)

  commandLine("npm", "install")
  workingDir(jsAppBuild)
}

val npmInstallFunctionsTask = tasks.register<Exec>("npmInstallFunctions") {
  dependsOn(copyJsAppFilesTask)

  inputs.file(packageJsonFunctions)
  outputs.dir(nodeModulesFunctions)
  outputs.file(packageLockJsonFunctions)

  commandLine("npm", "install")
  workingDir(functionsBuild)
}

val npmInstallAllTask = tasks.register("npmInstall") {
  dependsOn(npmInstallAppTask)
  dependsOn(npmInstallFunctionsTask)
}

val copyJsAppDistTask = tasks.register<Copy>("copyJsAppDist") {
  dependsOn(tasks.named("jsBrowserDistribution"))
  dependsOn(npmInstallAllTask)

  from(distributions)
  into(jsAppBuild.resolve("dist"))
}

val copyJsAppIndexHtmlTask = tasks.register<Copy>("copyJsAppIndexHtml") {
  dependsOn(tasks.named("jsBrowserDistribution"))
  dependsOn(npmInstallAllTask)

  from(distributions.resolve("index.html"))
  into(jsAppBuild)
}

val jsDistribution = tasks.register<Zip>("jsDistribution") {
  dependsOn(npmInstallAllTask)
  dependsOn(copyJsAppDistTask)
  dependsOn(copyJsAppIndexHtmlTask)

  from(jsAppBuild)
  destinationDirectory.set(outputDir)
}

val gcloudDeploy = tasks.register<Exec>("gcloudDeploy") {
  dependsOn(jsDistribution)

  workingDir(jsAppBuild)
  commandLine("gcloud", "app", "deploy")
}

val firebaseDeploy = tasks.register<Exec>("firebaseDeploy") {
  dependsOn(jsDistribution)
  dependsOn(gcloudDeploy)

  workingDir(jsAppBuild)
  commandLine("firebase", "deploy", "--only", "functions")
}

tasks.register("jsDeploy") {
  dependsOn(gcloudDeploy)
  dependsOn(firebaseDeploy)
}
