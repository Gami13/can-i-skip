plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.gami.can_i_skip"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gami.can_i_skip"
        minSdk = 24
        targetSdk = 33
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("io.github.wulkanowy:sdk:2.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.compose.material3:material3-android:1.2.0-alpha09")
    implementation("com.google.accompanist:accompanist-themeadapter-material3:0.33.2-alpha")
    {
        exclude(group = "androidx.compose.material3", module = "material3")
    }
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation("com.github.skydoves:colorpicker-compose:1.0.5")
    implementation("com.google.android.gms:play-services-ads:22.4.0")


}
