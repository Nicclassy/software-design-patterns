package structural

class SomeExpensiveObject(val anIdentifier: String, val anotherIdentifier: Int)

object FlyweightForExpensiveObjects {
    private val objects: MutableList<SomeExpensiveObject> = mutableListOf()

    fun create(anIdentifier: String, anotherIdentifier: Int): SomeExpensiveObject = objects.firstOrNull {
        (it.anIdentifier.equals(anIdentifier, true) &&
         it.anotherIdentifier == anotherIdentifier)
            .also { result ->
                if (result)
                    println("Object with $anIdentifier and $anotherIdentifier already exists in flyweight")
            }
        } ?: SomeExpensiveObject(anIdentifier, anotherIdentifier).also {
            objects.add(it)
        }
}

data class NamedObject(val name: String, val expensiveComponent: SomeExpensiveObject)

fun main() {
    val a = FlyweightForExpensiveObjects.create("A", 1)
    val b = FlyweightForExpensiveObjects.create("B", 2)
    val namedA = NamedObject("First letter", a)
    val namedB = NamedObject("Second letter", b)

    val flyweightA = FlyweightForExpensiveObjects.create("A", 1)
    val namedFlyweightA = NamedObject("Also the first letter", flyweightA)

    println(namedA)
    println(namedFlyweightA)
    println(namedB)
}