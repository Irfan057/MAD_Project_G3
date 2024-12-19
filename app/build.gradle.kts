plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.madprojectg3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.madprojectg3"
        minSdk = 33
        targetSdk = 34
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

   
    implementation("com.squareup.okhttp3:okhttp:4.10.0")


    implementation("com.google.code.gson:gson:2.8.8")


    implementation ("com.github.bumptech.glide:glide:4.15.1") //For Glide
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    // Other dependencies
    implementation(libs.tracing.perfetto.handshake)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
