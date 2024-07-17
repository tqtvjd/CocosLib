plugins {
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

android {
    namespace = "com.tqtvjd.view.xedittext"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.tqtvjd.view"
            artifactId = "xedittext"
            version = "1.0"
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
}