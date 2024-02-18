import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.sqldelight)
}

buildkonfig {
    packageName = "org.example.project"

    // default config if required
    defaultConfigs {
        buildConfigField(FieldSpec.Type.BOOLEAN, "MOCK", "false")
    }

    targetConfigs("staging") {
        create("android") {
            buildConfigField(FieldSpec.Type.BOOLEAN, "MOCK", "true")
            buildConfigField(FieldSpec.Type.STRING, "target", "android")
        }
        create("ios") {
            buildConfigField(FieldSpec.Type.BOOLEAN, "MOCK", "true")
            buildConfigField(FieldSpec.Type.STRING, "target", "ios")
        }
    }

    targetConfigs("release") {
        create("android") {
            buildConfigField(FieldSpec.Type.BOOLEAN, "MOCK", "true")
            buildConfigField(FieldSpec.Type.STRING, "target", "android")
        }
        create("ios") {
            buildConfigField(FieldSpec.Type.BOOLEAN, "MOCK", "true")
            buildConfigField(FieldSpec.Type.STRING, "target", "ios")
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.sqldelight.android.driver)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlinx.datetime)
            implementation(libs.mvvm.core)
            implementation(libs.mvvm.compose)
            implementation(libs.mvvm.flow)
            implementation(libs.mvvm.flow.compose)
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sqldelight {
        databases {
            create("ContactDatabase") {
                packageName.set("org.example.project.database")
            }
        }
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

