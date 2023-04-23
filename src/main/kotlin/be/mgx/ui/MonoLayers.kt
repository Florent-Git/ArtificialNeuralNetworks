package be.mgx.ui

import be.mgx.networks.monolayer.FourClassClassification
import be.mgx.networks.monolayer.ThreeClassClassification
import picocli.CommandLine

@CommandLine.Command(
    name = "perceptron-mono",
    subcommands = [
        ThreeClassClassification::class,
        FourClassClassification::class
    ]
)
object MonoLayers: AbstractCommandRunner<MonoLayerCommands, AbstractNetworkRunner>(MonoLayerCommands::class.java), LessonExample

enum class MonoLayerCommands(
    override val label: String,
    override val commandClassProvider: () -> AbstractNetworkRunner
): ICommandEnum<AbstractNetworkRunner> {
    THREECLASS("Classification à 3 classes", { AbstractNetworkRunner(ThreeClassClassification()) }),
    FOURCLASS("Classification à 4 classes", { AbstractNetworkRunner(FourClassClassification()) })
}