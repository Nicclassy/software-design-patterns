package behavioural

import java.lang.ref.WeakReference

abstract class Notification(open val data: Any)

abstract class Observer {
    abstract fun notify(notification: Notification)
}

abstract class Subject {
    abstract fun addObserver(observer: Observer)
    abstract fun removeObserver(observer: Observer)
    abstract fun notifyObservers(notification: Notification)
}

class UIUpdateNotification(override val data: String) : Notification(data)

class UIUpdateObserver(private val name: String) : Observer() {
    override fun notify(notification: Notification) {
        println("[${name}] UI update notification received: ${notification.data}")
    }
}

inline fun concurrently(block: () -> Unit) {
    // Pretend that the work is done concurrently
    block()
}

class UIText(
    private var text: String,
    private val observers: MutableList<WeakReference<Observer>> = mutableListOf()
) : Subject() {
    fun setText(text: String) {
        this.text = text
        notifyObservers(UIUpdateNotification("Text updated to '$text'"))
    }
    override fun addObserver(observer: Observer) {
        synchronized(this) {
            concurrently {
                observers.add(WeakReference(observer))
            }
        }
    }

    override fun removeObserver(observer: Observer) {
        synchronized(this) {
            concurrently {
                observers.remove(WeakReference(observer))
            }
        }
    }

    override fun notifyObservers(notification: Notification) {
        synchronized(this) {
            concurrently {
                observers.forEach { it.get()?.notify(notification) }
            }
        }
    }

    private fun disposeObservers() {
        observers.indices.forEach {
            val observer = observers[it].get()
            if (observer == null) observers.removeAt(it)
        }
    }
}

fun main() {
    val textElement = UIText("Hello, World!")
    val firstObserver = UIUpdateObserver("UI Observer 1")
    val secondObserver = UIUpdateObserver("UI Observer 2")
    val thirdObserver = UIUpdateObserver("UI Observer 3")
    textElement.addObserver(firstObserver)
    textElement.addObserver(secondObserver)
    textElement.addObserver(thirdObserver)
    textElement.setText("No longer hello world")

    textElement.removeObserver(thirdObserver)
    textElement.setText("The text has changed again")
}