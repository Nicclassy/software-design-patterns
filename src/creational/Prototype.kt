package creational

enum class GhostColour {
    RED, PINK, BLUE, ORANGE
}

class EntityId(val value: Int)

abstract class GameEntityPrototype<T>(
    open var id: EntityId,
    open var position: Pair<Int, Int>,
    open var symbol: Char,
) {
    abstract fun copy(): T
    abstract fun clone(): T
}

data class Pacman(
    override var id: EntityId,
    override var position: Pair<Int, Int>,
    override var symbol: Char,
    var isAlive: Boolean = true
) : GameEntityPrototype<Pacman>(id, position, symbol) {
    override fun copy(): Pacman = Pacman(id, position, symbol, isAlive)

    override fun clone(): Pacman {
        val clonedId = EntityId(id.value)
        val clonedPosition = Pair(position.first, position.second)
        return Pacman(clonedId, clonedPosition, symbol, isAlive)
    }
}

data class Ghost(
    override var id: EntityId,
    override var position: Pair<Int, Int>,
    override var symbol: Char,
    var colour: GhostColour
) : GameEntityPrototype<Ghost>(id, position, symbol) {
    override fun copy(): Ghost = Ghost(id, position, symbol, colour)

    override fun clone(): Ghost {
        val clonedId = EntityId(id.value)
        val clonedPosition = Pair(position.first, position.second)
        return Ghost(clonedId, clonedPosition, symbol, colour)
    }
}

fun main() {
    val pacman = Pacman(EntityId(0), Pair(1, 1), 'P', true)
    println("Is copied pacman equal to pacman? " + if (pacman == pacman.copy()) "Yes" else "No")
    println("Is cloned pacman equal to pacman? " + if (pacman == pacman.clone()) "Yes" else "No")

    val blinky = Ghost(EntityId(1), Pair(6, 6), 'B', GhostColour.RED)
    val clyde = blinky.clone()
    clyde.colour = GhostColour.BLUE
}