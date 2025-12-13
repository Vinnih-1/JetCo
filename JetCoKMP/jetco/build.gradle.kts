import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "com.developerstring.jetco_kmp"
version = "1.0.0"

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    js {
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.executable()
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "JetCo"
            isStatic = true
        }
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                enabled = false
            }
            commonWebpackConfig {
                outputFileName = "jetco.js"
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.material.icons.extended)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        iosMain.dependencies {
            // Add iOS-specific dependencies here if needed
        }
        wasmJsMain.dependencies {
            // Add WASM-specific dependencies here if needed
        }
    }
}

android {
    namespace = "com.developerstring.jetco_kmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        disable += "NullSafeMutableLiveData"
        abortOnError = false
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.developerstring.jetco_kmp.resources"
    generateResClass = always
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.developerstring.jetco_kmp.DesktopPreviewKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jetco-preview"
            packageVersion = "1.0.0"
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates("com.developerstring.jetco-kmp", "ui", "1.0.0-beta.9")

    pom {
        name = "JetCo"
        description = "A versatile Jetpack Compose library for building modern UI for your KMP Apps."
        inceptionYear = "2025"
        url = "https://github.com/developerchunk/jetco/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "developerchunk"
                name = "Developer Chunk"
                url = "https://github.com/developerchunk/jetco/"
            }
        }
        scm {
            url = "https://github.com/developerchunk/jetco/"
            connection = "scm:git:git://github.com/developerchunk/jetco.git"
            developerConnection = "scm:git:ssh://git@github.com/developerchunk/jetco.git"
        }

        withXml {
            val repo = asNode().appendNode("repositories").appendNode("repository")
            repo.appendNode("name", "Google")
            repo.appendNode("id", "google")
            repo.appendNode("url", "https://maven.google.com/")
        }
    }
}
