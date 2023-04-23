package be.mgx.networks.monolayer

import be.mgx.core.*
import be.mgx.core.math.Matrix
import be.mgx.functions.ActivationFunction
import be.mgx.util.retrieveDataFromCsv
import be.mgx.util.retrieveNetworkModelFromFile
import be.mgx.util.saveNetworkModelToFile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import picocli.CommandLine.*
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalSerializationApi::class)
@Command(name = "three-class", abbreviateSynopsis = true)
class ThreeClassClassification : IAbstractNetwork {
    @Command
    override fun init(
        @Option(names = ["-m", "--model"])
        model: File,
    ): Int {
        val network = NeuralNetwork.createNetwork(
            Layers.createLayer(2, 3, ActivationFunction.LINEAR)
        )

        val fileStream = FileOutputStream(model)
        Json.encodeToStream<NeuralNetwork>(network, fileStream)

        return 0
    }

    @Command
    override fun train(
        @Option(names = ["-m", "--model"])
        modelFile: File,
        @Option(names = ["-d", "--data"], description = ["Input file containing the training data"])
        dataFile: File
    ): Int {
        val network = retrieveNetworkModelFromFile(modelFile)
        val model = retrieveDataFromCsv(dataFile)

        var (X, Y) = model.transpose()
            .withIndex()
            .groupBy { it.index <= 1 }
            .map { it.value.map { v -> v.value } }
            .map { it.transpose() }

        X = X.map { x -> listOf(1.0) + x } // Prepend 1.0 to make the matrix a 1x3 input matrix

        network.train(
            X.map { x -> Matrix.createMatrix(1, 3) { x } },
            Y.map { y -> Matrix.createMatrix(1, 3) { y } },
            0.001,
            ErrorFunctions.simpleGradientError(1),
            StopFunctions.iterationStopFunction(300),
            listOf(saveLayerWeights)
        )

        saveNetworkModelToFile(network, modelFile)

        return 0
    }

    @Command
    override fun execute(
        @Option(names = ["-m", "--model"])
        model: File,
        @Parameters(description = ["Inputs (separated by commas) for the neural network"], split = ",")
        inputs: List<Double>
    ): Int {
        val network = retrieveNetworkModelFromFile(model)
        val input = Matrix.createMatrix(1, inputs.size) { inputs }

        val output = network.fire(input)

        println("Output: $output")

        return 0
    }
}