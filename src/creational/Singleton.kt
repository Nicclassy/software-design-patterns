package creational

class SwiftAppDelegate private constructor() {
    companion object {
        var shared: SwiftAppDelegate? = null
            get() {
                field = field ?: SwiftAppDelegate()
                return field
            }
            private set
    }
}

class LazyInitialisedSingleton private constructor() {
    companion object {
        val instance: LazyInitialisedSingleton by lazy { LazyInitialisedSingleton() }
    }
}

object LanguageLevelConfigurationSingleton {
    const val LOGGING_ENABLED = true
    const val REPOSITORY_NAME = "software-design-patterns"
}

fun main() {
    val singletonInstance = SwiftAppDelegate.shared
    val alsoSingletonInstance = SwiftAppDelegate.shared
    assert(singletonInstance === alsoSingletonInstance)

    println(LanguageLevelConfigurationSingleton.LOGGING_ENABLED)
    println(LanguageLevelConfigurationSingleton.REPOSITORY_NAME)

    val lazySingletonInstance = LazyInitialisedSingleton.instance
    val otherLazySingletonInstance = LazyInitialisedSingleton.instance
    assert(lazySingletonInstance === otherLazySingletonInstance)
}