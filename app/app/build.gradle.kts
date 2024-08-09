plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.solutions.note_it"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.solutions.note_it"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "NOTE_IT_API_BASE_URL",
            "\"${project.findProperty("noteIt.api.url")}\"")

        buildConfigField(
            "String",
            "NOTE_IT_API_AUDIENCE",
            "\"${project.findProperty("noteIt.api.audience")}\"")

        buildConfigField(
            "String",
            "AUTH0_DOMAIN",
            "\"${project.findProperty("auth0.domain")}\"")

        buildConfigField(
            "String",
            "AUTH0_SCHEME",
            "\"${project.findProperty("auth0.scheme")}\"")

        buildConfigField(
            "String",
            "AUTH0_CLIENT_ID",
            "\"${project.findProperty("auth0.client")}\"")

        manifestPlaceholders["auth0Domain"] = "${project.findProperty("auth0.domain")}"
        manifestPlaceholders["auth0Scheme"] = "${project.findProperty("auth0.scheme")}"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.gson)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.auth0)
}

kapt {
    correctErrorTypes = true
}
