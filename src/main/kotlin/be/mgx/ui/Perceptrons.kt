package be.mgx.ui

import be.mgx.networks.BasicAndPerceptron
import be.mgx.networks.LinearDataClassificationPerceptron
import picocli.CommandLine

@CommandLine.Command(
    name = "perceptron",
    subcommands = [
        BasicAndPerceptron::class,
        LinearDataClassificationPerceptron::class
    ]
)
object Perceptrons: AbstractCommandRunner<PerceptronCommands, AbstractNetworkRunner>(PerceptronCommands::class.java), LessonExample

enum class PerceptronCommands(
    override val label: String,
    override val commandClassProvider: () -> AbstractNetworkRunner
): ICommandEnum<AbstractNetworkRunner> {
    BASICAND("Opérateur logique ET", { AbstractNetworkRunner(BasicAndPerceptron()) }),
    LINEARCLASS("Classification de données linéairement séparables", { AbstractNetworkRunner(
        LinearDataClassificationPerceptron()
    ) })
    ;

    override fun toString(): String {
        return label
    }
}
