plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    id(libs.plugins.maven.publish.get().pluginId)
    id(libs.plugins.dokka.gradle.plugin.get().pluginId)
}

android {
    namespace = "com.pavanpm.spanwiz.library"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.foundation.android)
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    dokkaPlugin(libs.android.documentation.plugin)

}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val dokkaHtmlJar by tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-doc")
}
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.pmpavan.spanwiz"
            artifactId = "spanwiz"
            version = "0.0.1"
            artifact(dokkaJavadocJar)
            artifact(dokkaHtmlJar)
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(layout.projectDirectory.dir("../docs"))
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false) // Set to true if you want to avoid linking to Android SDK
        }
    }
}