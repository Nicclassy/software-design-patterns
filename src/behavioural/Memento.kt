package behavioural

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CodeFileState(val filename: String, val content: String, val time: LocalDateTime = LocalDateTime.now())

data class FileMemento(private val file: CodeFileState) {
    fun savedState(): CodeFileState = file
}

class CodeEditorOriginator(private var filename: String, private var file: CodeFileState) {
     companion object {
         val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mma")
     }

    constructor(filename: String, content: String = "") : this(filename, CodeFileState(filename, content))

    fun setContent(content: String) {
        file = CodeFileState(filename, content)
    }

    fun saveToMemento(): FileMemento = FileMemento(file)

    fun restore(memento: FileMemento) {
        val savedState = memento.savedState()
        // In this specific circumstance, copy the state but with the current timestamp.
        // This is what would actually happen in a code editor
        file = CodeFileState(savedState.filename, savedState.content)
    }

    fun printCode() = println(file.content)

    fun printDate() = println("Saved file ${file.filename} at ${formatter.format(file.time)}")
}

class CodeEditorCaretaker(
    private val codeEditor: CodeEditorOriginator,
    private val mementos: MutableList<FileMemento> = mutableListOf()
) {
    fun save() {
        codeEditor.printDate()
        mementos.add(codeEditor.saveToMemento())
    }

    fun undo() {
        if (mementos.isEmpty()) {
            println("No more changes to undo.")
            return
        }
        
        val lastMemento = mementos.removeLast()
        codeEditor.restore(lastMemento)
    }
}

fun main() {
    val codeEditor = CodeEditorOriginator("Main.java")
    val caretaker = CodeEditorCaretaker(codeEditor)

    codeEditor.setContent("public static")
    caretaker.save()
    codeEditor.setContent("public static void")
    caretaker.save()
    codeEditor.setContent("public static void main")
    caretaker.save()
    codeEditor.setContent("public static void main(String[] args)")

    repeat(4) {
        codeEditor.printCode()
        caretaker.undo()
    }
}