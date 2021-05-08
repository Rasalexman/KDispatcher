package appdependencies

object Libs {
    object Core {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val anko = "org.jetbrains.anko:anko:${Versions.anko_version}"
        const val ankoDesign = "org.jetbrains.anko:anko-design:${Versions.anko_version}"
        const val kdispatcher = "com.rasalexman.kdispatcher:kdispatcher:${Builds.KDispatcher.VERSION_NAME}"
    }

    object Tests {
        const val junit = "junit:junit:${Versions.junit}"
        const val runner = "com.android.support.test:runner:${Versions.runner}"
        const val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
    }
}