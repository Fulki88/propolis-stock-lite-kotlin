pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "propolis-stock-lite-kotlin"

include(":app")
include(":core:common")
include(":core:designsystem")
include(":core:data")
include(":feature:inventory")
include(":feature:sales")
