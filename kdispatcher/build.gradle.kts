import appdependencies.Versions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import resources.Resources.codeDirs

plugins {
    id("java-library")
    id("kotlin")
    id("org.jetbrains.dokka")
}

// Declare the task that will monitor all configurations.
configurations.all {
    // 2 Define the resolution strategy in case of conflicts.
    resolutionStrategy {
        // Fail eagerly on version conflict (includes transitive dependencies),
        // e.g., multiple different versions of the same dependency (group and name are equal).
        failOnVersionConflict()

        // Prefer modules that are part of this build (multi-project or composite build) over external modules.
        preferProjectModules()
    }
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.suppressWarnings = true
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.noReflect = true
    kotlinOptions.freeCompilerArgs += listOf(
            "-XXLanguage:+InlineClasses"
    )
}

sourceSets {
    getByName("main") {
        java.setSrcDirs(codeDirs)
    }
}

dependencies {
    implementation(kotlin("stdlib", Versions.kotlin))
}

tasks {
    val dokkaOutputDir = "$buildDir/dokka"

    dokkaHtml {
        outputDirectory.set(file(dokkaOutputDir))
    }

    val deleteDokkaOutputDir by registering(Delete::class) {
        delete(dokkaOutputDir)
    }

    register<Jar>("javadocJar") {
        dependsOn(deleteDokkaOutputDir, dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaOutputDir)
    }
}

repositories {
    mavenCentral()
}
// comment this apply function if you fork this project
apply {
    from("deploy.gradle")
}
