import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.library")
    alias(libs.plugins.kotlinSerialization)
    id("maven-publish")
}

//project var
val projectName by extra { "ktorUtils" }
val projectVersion by extra { "1.0.0" }

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
        publishLibraryVariants("release")
        //publishAllLibraryVariants()
    }

    jvm("desktop"){
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }

    //    iosArm64()   // 64-bit iPhone devices
    //    macosArm64() // Modern Apple Silicon-based Macs
    //    watchosX64() // Modern 64-bit Apple Watch devices
    //    tvosArm64()  // Modern Apple TV devices
    val xcf = XCFramework(getVersionNameIOS())
    listOf(
        iosX64(),
        iosArm64(),
      //  iosSimulatorArm64()
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

        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.kotlin.serialization)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
        }

        androidMain.dependencies {
//            implementation(libs.ktor.client.android)
//            implementation(libs.ktor.client.okhttp)
        }


        iosMain.dependencies {
//            implementation(libs.ktor.client.darwin)
        }

        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation(libs.ktor.client.android)
//            implementation(libs.ktor.client.okhttp)
        }
    }
}

//compose.desktop {
//    application {
//        mainClass = "MainKt"
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = projectName
//            packageVersion = projectVersion
//        }
//    }
//}

android {
    namespace = "com.weredev.ktorUtils"
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }


    libraryVariants.all{
        outputs.all {
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = "${getVersionName(name)}.aar"
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}


//publishing {
//    publications {
//        publications.configureEach {
//            if (this is MavenPublication) {
//                pom {
//                    name.set(projectName)
//                    description.set("Ktor utils functions")
//                    //url.set(pkgUrl)
//                }
//            }
//        }
////        create<MavenPublication>("maven") {
////        withType<MavenPublication> {
////            groupId = "$group"
////            artifactId = artifact
////            version = version
////            artifact(dokkaJar)
////        }
//    }
//}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.weredevelopers"
                artifactId = "ktorUtils"
                version = "1.0.0"
            }
        }
    }
}


fun getVersionName(name: String? = null): String {
    var newName = projectName + "_kmp_v" + projectVersion
    if(name != null && !name.equals("release")) {
        newName += "_$name"
    }
    return newName
}

fun getVersionNameIOS(name: String? = null): String {
    var newName = projectName + "_kmp"
    if(name != null && !name.equals("release")) {
        newName += "_$name"
    }
    return newName
}