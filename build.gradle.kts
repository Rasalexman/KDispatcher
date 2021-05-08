// Top-level build file where you can add configuration options common to all sub-projects/modules.
import appdependencies.ClassPath

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/kotlin/dokka") }

    }
    dependencies {
        classpath(appdependencies.ClassPath.gradle)
        classpath(appdependencies.ClassPath.kotlingradle)

        classpath(appdependencies.ClassPath.google)
        classpath(appdependencies.ClassPath.mavenplugin)
        classpath(appdependencies.ClassPath.dokkaplugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/kotlin/dokka") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}