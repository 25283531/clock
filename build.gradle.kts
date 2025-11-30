// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
}

// Gradle 9.2.0 compatibility configuration
allprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-parameters")
    }
}

// Configure repositories to use secure protocols and avoid deprecated features
allprojects {
    repositories {
        // Ensure secure repositories are used
        mavenCentral()
        google()
    }
}
