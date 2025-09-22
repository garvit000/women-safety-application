plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") // Added Google Services plugin
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {

    // implementation(libs.androidx.core.ktx) // Replaced with direct version
    implementation("androidx.core:core-ktx:1.10.1") // Added direct version
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    // implementation(libs.androidx.constraintlayout) // Replaced with direct version
    implementation("androidx.constraintlayout:constraintlayout:2.2.1") // Updated to latest stable version
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Firebase Bill of Materials (BOM)
    implementation(platform("com.google.firebase:firebase-bom:33.1.1")) // Ensure this is the latest version

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firebase Realtime Database (for emergency contacts, etc.)
    implementation("com.google.firebase:firebase-database-ktx")

    // Firebase Cloud Messaging (for push notifications)
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Google Maps API (will be needed for location tracking)
    implementation("com.google.android.gms:play-services-maps:18.2.0") // Ensure this is the latest version
    implementation("com.google.android.gms:play-services-location:21.3.0") // For location services


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}