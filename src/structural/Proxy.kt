package structural

enum class Permission {
    SUFFICIENT,
    INSUFFICIENT
}

abstract class ExpensiveResource {
    abstract fun show()
}

class LargeVideo(private val name: String): ExpensiveResource() {
    override fun show() {
        println("Playing video '$name'")
    }
}

class AccessControlledResourceProxy(
    private val resource: ExpensiveResource,
    private val permission: Permission
): ExpensiveResource() {
    init {
        println("Created access controlled resource")
    }

    override fun show() {
        if (permission == Permission.INSUFFICIENT) {
            println("Cannot view resource")
            return
        }

        println("Sufficient permissions to access the resource")
        resource.show()
    }
}

class LazyLoadedResourceProxy(
    private val createResource: () -> ExpensiveResource,
    private var resource: ExpensiveResource? = null
): ExpensiveResource() {
    init {
        println("Created lazy loaded resource")
    }

    override fun show() {
        if (resource == null) {
            resource = createResource()
            println("Successfully Lazy loaded resource")
        } else {
            println("Resource already lazy loaded")
        }

        resource!!.show()
    }
}

fun main() {
    val lazyLoadedVideo = LazyLoadedResourceProxy({ LargeVideo("documentary.mp3") })
    val accessControlledResource = AccessControlledResourceProxy(lazyLoadedVideo, Permission.SUFFICIENT)
    println("Showing video")
    accessControlledResource.show()
}