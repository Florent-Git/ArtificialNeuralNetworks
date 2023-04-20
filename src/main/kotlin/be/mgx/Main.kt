package be.mgx

import be.mgx.networks.BasicAndPerceptron
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
        BasicAndPerceptron::class
    ]
)
object App
