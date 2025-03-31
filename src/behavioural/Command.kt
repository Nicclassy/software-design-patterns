package behavioural

enum class GithubRepository {
    AvailableRepository,
    UnavailableRepository;
}

fun GithubRepository.connect(): Boolean = this == GithubRepository.AvailableRepository

data class GithubUser(val name: String, val email: String)

abstract class GithubCommand {
    abstract fun execute()
    abstract fun name(): String
}

class ConfigCommand(private val user: GithubUser) : GithubCommand() {
    override fun execute() {
        println("Name: ${user.name}\nEmail: ${user.email}")
    }

    override fun name(): String = "config"
}

class CommitCommand(
    private val author: String,
    private val message: String,
    private val files: List<String>
) : GithubCommand() {
    override fun execute() {
        println("$author commits changes to ${files.joinToString(",") }} with message $message")
    }

    override fun name(): String = "commit"
}

class PushCommand(private val repository: GithubRepository) : GithubCommand() {
    private fun repositoryIsAvailable(repository: GithubRepository): Boolean = repository.connect()

    override fun execute() {
        if (repositoryIsAvailable(repository))
            println("Repository is available. Pushing changes")
        else
            println("Repository is unavailable")
    }

    override fun name(): String = "push"
}

class GithubCommandExecutor(private val commands: MutableList<GithubCommand> = mutableListOf()) {
    fun addCommand(command: GithubCommand) {
        commands.add(command)
    }

    fun executeCommands(verbose: Boolean = false) {
        commands.forEach {
            if (verbose)
                println("## Executing command '${it.name()}' ##")
            it.execute()
            if (verbose)
                println("## Finished command '${it.name()}' ##\n")
        }
    }
}

fun main() {
    val githubCommands = GithubCommandExecutor()
    githubCommands.addCommand(ConfigCommand(GithubUser("John Smith", "john.smith@example.com")))
    githubCommands.addCommand(
        CommitCommand("John Smith", "Initial Commit", mutableListOf("main.py", "misc.py"))
    )
    githubCommands.addCommand(PushCommand(GithubRepository.AvailableRepository))
    githubCommands.executeCommands(verbose = true)
}