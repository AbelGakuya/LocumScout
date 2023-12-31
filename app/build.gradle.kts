plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.locums.locumscout"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.locums.locumscout"
        minSdk = 24
        targetSdk = 33
        versionCode = 2 // Update the version code to a unique integer
        versionName = "1.1" // Update the version name to reflect the new version
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            buildConfigField("String",
//                "FCM_SERVER_KEY",
//                "\"AAAAqsaAwzM:APA91bHh43O6I5gmke4lmMn1Gzi6MLaANCM4aIR1A8Vgw_bCEemd_s9AcFfE6699dsDzDxapedp5QaUCNVjiJFgvizIXU_AqiR07UOANGJGDl2N_AqJUj3b45WdjEd3XPoc-zFgE1FUi\"")

        }

//        debug {
//            buildConfigField("String",
//                "FCM_SERVER_KEY",
//                "\"AAAAqsaAwzM:APA91bHh43O6I5gmke4lmMn1Gzi6MLaANCM4aIR1A8Vgw_bCEemd_s9AcFfE6699dsDzDxapedp5QaUCNVjiJFgvizIXU_AqiR07UOANGJGDl2N_AqJUj3b45WdjEd3XPoc-zFgE1FUi\"")
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.0")
    implementation("com.google.firebase:firebase-firestore:24.7.0")
    implementation("com.google.firebase:firebase-storage:20.2.1")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.google.firebase:firebase-functions:20.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.recyclerview:recyclerview:1.3.1")

    //Material library
    implementation ("com.google.android.material:material:1.4.0")


    // Navigation Components
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

    //circleimageview dependency
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //cardview
    implementation("androidx.cardview:cardview:1.0.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    //google play services
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    //Firebase
    implementation ("com.google.firebase:firebase-core:20.0.0")
    implementation ("com.google.firebase:firebase-messaging:23.0.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")

    //WorkManager
    implementation("androidx.work:work-runtime:2.8.1")

    //Viewpager
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.9.0")

    //timeAgo
  //  implementation("com.github.marlonlom:timeago:4.0.2")


}