import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
}

android {
    namespace = "it.unimib.socialmesh"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.unimib.socialmesh"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "events_api_key", gradleLocalProperties(rootDir).getProperty("events_api_key"))
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
    buildToolsVersion = "34.0.0"
}


dependencies {
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-base:18.2.0")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    implementation("androidx.room:room-common:2.6.1")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("commons-validator:commons-validator:1.7")
    implementation("com.facebook.android:facebook-android-sdk:[8,9)")
    implementation("androidx.transition:transition:1.4.0")
    implementation("commons-io:commons-io:2.15.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20230227")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation("androidx.room:room-runtime:2.6.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.appcompat:appcompat:1.4.0")

}
