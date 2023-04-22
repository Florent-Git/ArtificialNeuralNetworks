package be.mgx

import be.mgx.ui.AbstractCommandRunner
import be.mgx.ui.ICommandEnum
import be.mgx.ui.LessonExample
import be.mgx.ui.Perceptrons
import picocli.CommandLine
import picocli.CommandLine.Command
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val exitCode = CommandLine(App)
        .execute(*args)
    exitProcess(exitCode);
}

@Command(
    name = "neutron",
    subcommands = [
        Perceptrons::class
    ]
)
object App: AbstractCommandRunner<AppCommands, LessonExample>(AppCommands::class.java)

enum class AppCommands(
    override val label: String,
    override val commandClassProvider: () -> LessonExample
): ICommandEnum<LessonExample> {
    PERCEPTRON("Perceptron", { Perceptrons }),
    // MONO("Perceptron monocouche"),
    // MULTI("Perceptron multicouche")
    ;

    override fun toString(): String {
        return label
    }
}
