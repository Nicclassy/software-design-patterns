package creational

abstract class UIElement
abstract class Icon(open val width: Int, open val height: Int) : UIElement()
abstract class Button(open val text: String) : UIElement()
abstract class Toggle(open val active: Boolean) : UIElement()

data class MacOSIcon(override val width: Int, override val height: Int) : Icon(width, height)
data class MacOSButton(override val text: String) : Button(text)
data class MacOSToggle(override val active: Boolean) : Toggle(active)

data class WindowsIcon(override val width: Int, override val height: Int) : Icon(width, height)
data class WindowsButton(override val text: String) : Button(text)
data class WindowsToggle(override val active: Boolean) : Toggle(active)

abstract class UIElementFactory {
    abstract fun createIcon(width: Int, height: Int): Icon
    abstract fun createButton(text: String): Button
    abstract fun createToggle(active: Boolean): Toggle
}

class MacOSFactory : UIElementFactory() {
    override fun createIcon(width: Int, height: Int) = MacOSIcon(width, height)
    override fun createButton(text: String) = MacOSButton(text)
    override fun createToggle(active: Boolean) = MacOSToggle(active)
}

class WindowsFactory : UIElementFactory() {
    override fun createIcon(width: Int, height: Int) = WindowsIcon(width, height)
    override fun createButton(text: String) = WindowsButton(text)
    override fun createToggle(active: Boolean) = WindowsToggle(active)
}

fun designSimpleUI(factory: UIElementFactory): List<UIElement> {
    val createButton = factory.createButton("Create")
    val deleteButton = factory.createButton("Delete")
    val icon = factory.createIcon(32, 32)
    val toggle = factory.createToggle(false)
    return listOf(createButton, deleteButton, icon, toggle)
}

fun main() {
    val windowsFactory = WindowsFactory()
    val windowsElements = designSimpleUI(windowsFactory)
    println("Windows Elements: $windowsElements")

    val macFactory = MacOSFactory()
    val macOSElements = designSimpleUI(macFactory)
    println("MacOS Elements: $macOSElements")
}