import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
}


//project var
val projectName by extra { "app-kmm" }
val projectVersion by extra { "1.0.0" }

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }


    jvm("desktop")
    
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }
    val xcf = XCFramework(getVersionNameIOS())
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {

        version = projectVersion
        it.binaries.framework {
            binaryOption("bundleVersion", projectVersion)
            binaryOption("bundleShortVersionString",projectVersion)
            baseName = getVersionNameIOS()
            xcf.add(this)
        }
    }
    
    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
//            implementation("io.ktor:ktor-client-ios:$ktorVersion")
            implementation(libs.ktor.client.darwin)
        }
        nativeMain.dependencies {
            //implementation(libs.coroutines.core.native)
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(project(":ktorUtils"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.google.gson)
            implementation(libs.kotlin.serialization)
            implementation(libs.napier)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)

            implementation(libs.koin.core)
            implementation(libs.coroutines.core)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.weredev.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.weredev.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = projectVersion
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

//    buildFeatures {
//        compose = true
//    }
//
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.11"
//    }

//    externalNativeBuild {
//        cmake {
//            path = file("src/androidMain/CMakeLists.txt")
//        }
//    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = projectName
            packageVersion = projectVersion
        }
    }
}



fun getVersionNameIOS(name: String? = null): String {
    var newName = projectName
    if(name != null && !name.equals("release")) {
        newName += "_$name"
    }
    return newName
}
