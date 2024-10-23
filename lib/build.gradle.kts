plugins {
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

android {
    namespace = "cc.xl"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
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
        create<MavenPublication>("ReleaseAar") {
            groupId = "cc.xl"
            artifactId = "lib"
            version = "1.0"
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.utilcodex)
    implementation(libs.xedittext)
}