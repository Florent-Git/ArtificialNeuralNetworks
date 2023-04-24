package be.mgx.ui

import be.mgx.networks.monolayer.FourClassClassification
import be.mgx.networks.monolayer.ThreeClassClassification
import be.mgx.networks.multilayer.NonLinearRegression
import be.mgx.networks.multilayer.ThreeClassMultiNonLinear
import be.mgx.networks.multilayer.TwoClassMultiNonLinear
import be.mgx.networks.multilayer.XorMultiPerceptron
import picocli.CommandLine

@CommandLine.Command(
    name = "perceptron-multi",
    subcommands = [
        XorMultiPerceptron::class,
        TwoClassMultiNonLinear::class,
        ThreeClassMultiNonLinear::class,
        NonLinearRegression::class
    ]
)
object MultiLayers: AbstractCommandRunner<MultiLayerCommands, AbstractNetworkRunner>(MultiLayerCommands::class.java)

enum class MultiLayerCommands(
    override val label: String,
    override val commandClassProvider: () -> AbstractNetworkRunner
): ICommandEnum<AbstractNetworkRunner> {
    XORMULTI("Opération logique XOR", { AbstractNetworkRunner(XorMultiPerceptron()) }),
    MULTI_NONLINEAR_CLASSIFICATION_TWO_CLASS("Classification a 2 classes non lineairement separables", { AbstractNetworkRunner(TwoClassMultiNonLinear()) }),
    MULTI_NONLINEAR_CLASSIFICATION_THREE_CLASS("Classification a 3 classes non lineairement separable", { AbstractNetworkRunner(TwoClassMultiNonLinear()) }),
    MULTI_NONLINEAR_REGRESSION("Regression lineaire", { AbstractNetworkRunner(TwoClassMultiNonLinear()) }),
}
