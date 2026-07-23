pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "artier-ide-lite"
include(":app")
include(":core:common")
include(":core:model")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:security")
include(":feature:editor")
include(":feature:terminal")
include(":feature:ai-panel")
include(":feature:file-explorer")
include(":feature:settings")
include(":ai:agent")
include(":ai:autocomplete")
include(":ai:quickedit")
include(":ai:checkpoint")
include(":ai:provider")
include(":ai:lint")
include(":daemon-bridge")
include(":ui")
