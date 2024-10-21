plugins {
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

android {
    namespace = "cn.sharerec"
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
            groupId = "cn.sharerec"
            artifactId = "shareREC"
            version = "1.0"
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}

dependencies {
    // 添加libs目录下的所有jar文件
    api (fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
}