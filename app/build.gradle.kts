plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    alias(libs.plugins.compose.compiler)
    id("androidx.room") version "2.6.1" apply false

}

android {
    namespace = "com.nnamanistephen.todo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nnamanistephen.todo"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "option_name" to "option_value"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true

    }
//    composeCompiler {
//        enableStrongSkippingMode = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.15"
//
//    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Compose dependencies
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material3)
    implementation(libs.ui)



    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    ksp (libs.dagger.compiler) // Dagger compiler
    ksp (libs.hilt.compiler)  // Hilt compiler

    //Room
    implementation(libs.room.ktx.v252)
    implementation(libs.androidx.room.runtime)
//    ksp(libs.androidx.room.compiler)
//    ksp(libs.androidx.room.compiler.v252)
//    ksp("androidx.room:room-compiler:2.5.0")
    implementation(libs.symbol.processing.api)
    ksp("androidx.room:room-compiler:2.5.0")



    // Kotlin extensions and Room coroutine support
    implementation(libs.androidx.room.ktx.v252)

    //Retrofit
    implementation(libs.retrofit)

    //GSON
    implementation(libs.gson)
    implementation(libs.converter.gson)
}