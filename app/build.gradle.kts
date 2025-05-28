plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.serialization)

    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.jean.cuidemonosaqp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jean.cuidemonosaqp"
        minSdk = 24
        targetSdk = 35
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
        compose = true
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
    //Libs
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Convertidor JSON con Gson (opcional, también puedes usar Moshi)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp (opcional para logging o personalización)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // OkHttp (para personalizar las peticiones, logs, interceptores, etc.)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
    kapt("com.google.dagger:hilt-compiler:2.56.2")

    // Integración con Jetpack Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
}