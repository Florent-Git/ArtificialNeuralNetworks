package be.mgx.ui

import be.mgx.networks.AdalineAndPerceptron
import be.mgx.networks.BasicAndPerceptron
import be.mgx.networks.GradientAndPerceptron
import picocli.CommandLine

@CommandLine.Command(
    name = "perceptron",
    subcommands = [
        BasicAndPerceptron::class,
        GradientAndPerceptron::class,
        AdalineAndPerceptron::class
    ]
)
object Perceptrons: AbstractCommandRunner<PerceptronCommands, AbstractNetworkRunner>(PerceptronCommands::class.java), LessonExample

enum class PerceptronCommands(
    override val label: String,
    override val commandClassProvider: () -> AbstractNetworkRunner
): ICommandEnum<AbstractNetworkRunner> {
    BASICAND("Opérateur logique ET", { AbstractNetworkRunner(BasicAndPerceptron()) }),
    GRADIENTAND("Opérateur logique ET avec Gradient", { AbstractNetworkRunner(GradientAndPerceptron()) }),
    ADALINEAND("Opérateur logique ET avec Adaline", { AbstractNetworkRunner(AdalineAndPerceptron()) })
    ;

    override fun toString(): String {
        return label
    }
}
