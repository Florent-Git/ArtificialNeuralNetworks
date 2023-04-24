package be.mgx.ui

import be.mgx.networks.monolayer.FourClassClassification
import be.mgx.networks.monolayer.ThreeClassClassification
import be.mgx.networks.multilayer.XorMultiPerceptron
import picocli.CommandLine

@CommandLine.Command(
    name = "perceptron-multi",
    subcommands = [
        XorMultiPerceptron::class
    ]
)
object MultiLayers: AbstractCommandRunner<MultiLayerCommands, AbstractNetworkRunner>(MultiLayerCommands::class.java)

enum class MultiLayerCommands(
    override val label: String,
    override val commandClassProvider: () -> AbstractNetworkRunner
): ICommandEnum<AbstractNetworkRunner> {
    XORMULTI("Opération logique XOR", { AbstractNetworkRunner(XorMultiPerceptron()) });
}
