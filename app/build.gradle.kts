plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.mobileapptechnobit"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobileapptechnobit"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
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
    implementation(libs.androidx.appcompat)
    implementation("com.android.volley:volley:1.2.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.activity:activity-compose:1.5.0")
    implementation ("androidx.compose.ui:ui:1.1.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.core)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing
    implementation(libs.truth)

    implementation("androidx.navigation:navigation-compose:2.7.5") // Update ke versi terbaru
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.compose.material3:material3:1.2.0") // Versi terbaru

    implementation ("androidx.compose.material:material-icons-extended:1.5.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.10.1")


    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("androidx.camera:camera-core:1.3.0-rc01")
    implementation("androidx.camera:camera-camera2:1.3.0-rc01")
    implementation("androidx.camera:camera-lifecycle:1.3.0-rc01")
    implementation("androidx.camera:camera-view:1.3.0-rc01")
    implementation("androidx.camera:camera-extensions:1.3.0-rc01")
    implementation ("com.google.android.gms:play-services-location:21.3.0")

    implementation ("com.google.accompanist:accompanist-permissions:0.20.3")

    implementation ("androidx.compose.ui:ui-graphics:1.3.1")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.compose.ui:ui:1.6.0")

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("io.mockk:mockk-android:1.13.5")
    testImplementation("androidx.arch.core:core-testing:2.2.0") // For LiveData testing

    // Integration Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")

    // Compose Testing (matching your compose version)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")

    // Permission Testing
    androidTestImplementation("androidx.test:rules:1.5.0")

    // Activity Testing
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    // Navigation Testing
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.5")

    // Camera Testing
//    androidTestImplementation(libs.androidx.camera.testing)

    // Network Testing
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    testImplementation("com.squareup.retrofit2:retrofit-mock:2.9.0")

    // Additional Testing Utilities
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    androidTestImplementation("org.mockito:mockito-android:5.3.1")

    // For testing coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    // Lifecycle Testing
    testImplementation("androidx.lifecycle:lifecycle-runtime-testing:2.7.0")

    // Image Loading Testing (for Coil)
    testImplementation("io.coil-kt.coil3:coil:3.0.0")

    // Truth assertions (you already have this as implementation, move to test)
    // Remove from main implementation and add as test dependency
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)

    implementation ("com.google.mlkit:barcode-scanning:17.0.3")

}