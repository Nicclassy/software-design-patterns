package structural

abstract class FileSystemComponent(open val name: String) {
    abstract fun add(component: FileSystemComponent)
    abstract fun remove(component: FileSystemComponent)
}

data class DirectoryComponent(
    override val name: String,
    var components: MutableList<FileSystemComponent> = mutableListOf()
) : FileSystemComponent(name) {
    override fun add(component: FileSystemComponent) {
        components.add(component)
    }

    override fun remove(component: FileSystemComponent) {
        components.remove(component)
    }
}

data class FileComponent(override val name: String) : FileSystemComponent(name) {
    override fun add(component: FileSystemComponent) =
        throw UnsupportedOperationException("Cannot add components to a file")

    override fun remove(component: FileSystemComponent) =
        throw UnsupportedOperationException("Cannot remove components from a file")
}

fun DirectoryComponent.listChildrenRecursively(depth: Int = 1) {
    println("${"-".repeat(depth)} $name")
    for (component in this.components) {
        if (component is DirectoryComponent) {
            component.listChildrenRecursively(depth + 1)
        } else {
            println("${"-".repeat(depth + 1)} ${component.name}")
        }
    }
}

fun DirectoryComponent.firstFileLexicographically(): FileComponent? {
    var result: FileComponent? = null
    for (component in this.components) {
        if (component is FileComponent) {
            if (result == null || component.name < result.name) {
                result = component
            }
        } else if (component is DirectoryComponent) {
            val directoryResult = component.firstFileLexicographically()
            result = if (directoryResult == null) result
                else if (result == null) directoryResult
                else if (directoryResult.name < result.name) directoryResult
                else result
        }
    }

    return result
}

fun main() {
    val home = DirectoryComponent("home")
    val other = DirectoryComponent("Other")
    val documents = DirectoryComponent("Documents")

    val cricketsSample = FileComponent("crickets_sample.mp3")
    val infoFile = FileComponent("info.txt")
    other.add(cricketsSample)
    other.add(infoFile)

    val kotlinFolder = DirectoryComponent("Kotlin")
    val kotlinManual = FileComponent("kotlin_manual.pdf")
    val program = FileComponent("Program.kt")
    kotlinFolder.add(kotlinManual)
    kotlinFolder.add(program)

    val aaronAardvark = FileComponent("AaronAardvark.txt")
    val script = FileComponent("script.py")
    documents.add(script)
    documents.add(aaronAardvark)
    documents.add(kotlinFolder)

    home.add(other)
    home.add(documents)

    println("Attempting to add file to components of another file")
    try {
        infoFile.add(program)
    } catch (e : UnsupportedOperationException) {
        println("Failed to add file to components of another file. Reason: ${e.message}")
    }

    home.listChildrenRecursively()
    val firstFileLexicographically = home.firstFileLexicographically()
    println("Name of first file lexicographically: ${firstFileLexicographically!!.name}")
}