package be.mgx.ui

import be.mgx.networks.*
import picocli.CommandLine

@CommandLine.Command(
    name = "perceptron",
    subcommands = [
        BasicAndPerceptron::class,
        GradientAndPerceptron::class,
        AdalineAndPerceptron::class,
        GradientLinearClassificationPerceptron::class,
        AdalineLinearClassificationPerceptron::class,
    ]
)
object Perceptrons: AbstractCommandRunner<PerceptronCommands, AbstractNetworkRunner>(PerceptronCommands::class.java), LessonExample

enum class PerceptronCommands(
    override val label: String,
    override val commandClassProvider: () -> AbstractNetworkRunner
): ICommandEnum<AbstractNetworkRunner> {
    BASICAND("Opérateur logique ET", { AbstractNetworkRunner(BasicAndPerceptron()) }),
    GRADIENTAND("Opérateur logique ET (Gradient)", { AbstractNetworkRunner(GradientAndPerceptron()) }),
    ADALINEAND("Opérateur logique ET (ADALINE)", { AbstractNetworkRunner(AdalineAndPerceptron()) }),
    GRADIENTLINEARCLASS("Classification de données linéairement séparables (Gradient)", { AbstractNetworkRunner(GradientLinearClassificationPerceptron()) }),
    ADALINELINEARCLASS("Classification de données linéairement séparables (ADALINE)", { AbstractNetworkRunner(AdalineLinearClassificationPerceptron()) }),
//    GRADIENTNONLINEARCLASS("Classification de données non linéairement séparables (Gradient)", { AbstractNetworkRunner() }),
//    ADALINENONLINEARCLASS("Classification de données non linéairement spéarables (ADALINE)", { AbstractNetworkRunner() })
    ;

    override fun toString(): String {
        return label
    }
}
