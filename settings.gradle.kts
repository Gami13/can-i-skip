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

        maven ( url = "https://s01.oss.sonatype.org/content/repositories/snapshots/" )
        maven (url= "https://jitpack.io" )
        maven(url ="https://repo1.maven.org/maven2/")

    }

}

rootProject.name = "can-i-skip"
include(":app")
