package structural

interface GameEngineRenderer {
    fun renderTerrain()
    fun renderEntities()
}

class UnityEngineRenderer : GameEngineRenderer {
    override fun renderTerrain() = println("Rendering Unity terrain")
    override fun renderEntities() = println("Rendering Unity entities")
}

class UnrealEngineRenderer : GameEngineRenderer {
    override fun renderTerrain() = println("Rendering Unreal terrain")
    override fun renderEntities() = println("Rendering Unreal entities")
}

abstract class GameObject {
    abstract fun render()
}

class Terrain(private val renderer: GameEngineRenderer) : GameObject() {
    override fun render() = renderer.renderTerrain()
}

class GameEntities(private val renderer: GameEngineRenderer) : GameObject() {
    override fun render() = renderer.renderEntities()
}

fun main() {
    val unityRenderer = UnityEngineRenderer()
    val unrealRenderer = UnrealEngineRenderer()

    val unityTerrain = Terrain(unityRenderer)
    val unrealTerrain = Terrain(unrealRenderer)
    unityTerrain.render()
    unrealTerrain.render()
}