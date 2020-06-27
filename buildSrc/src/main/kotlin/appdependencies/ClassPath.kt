package appdependencies

object ClassPath {

    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlingradle = "org.gradle.kotlin:plugins:${Versions.kotlinGradle}"
    const val google = "com.google.gms:google-services:${Versions.google}"
    const val bintrayplugin = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}"
    const val mavenplugin = "com.github.dcendents:android-maven-gradle-plugin:${Versions.maven}"
    const val dokkaplugin = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"
}