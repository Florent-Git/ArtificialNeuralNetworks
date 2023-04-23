package be.mgx.networks.perceptron

import be.mgx.core.*
import be.mgx.core.math.Matrix
import be.mgx.functions.ActivationFunction
import be.mgx.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import picocli.CommandLine.*
import java.io.File
import java.io.FileOutputStream

@Command(name = "linear-regression-adaline", abbreviateSynopsis = true)
class AdalineLinearRegressionPerceptron : IAbstractNetwork {
    @OptIn(ExperimentalSerializationApi::class)
    @Command
    override fun init(
        @Option(names = ["-m", "--model"])
        model: File,
    ): Int {
        val network = NeuralNetwork.createNetwork(
            Layers.createLayer(1, 1, ActivationFunction.LINEAR)
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

        var (X, Y) = model.map { it.chunked(1) }
            .transpose()

        X = X.map { x -> listOf(1.0) + x } // Prepend 1.0 to make the matrix a 1x2 input matrix

        network.train(
            X.map { x -> Matrix.createMatrix(1, 2) { x } },
            Y.map { y -> Matrix.createMatrix(1, 1) { y } },
            .00014,
            ErrorFunctions.simpleGradientError(X.size),
            StopFunctions.iterationStopFunction(8034),
            listOf(saveLayerWeights)
        )

        saveNetworkModelToFile(network, modelFile)

        val inputs = mutableListOf<ArrayList<Double>>()

        for (arrayList in model) {
            val newArrayList = arrayListOf(arrayList[0], arrayList[1])
            inputs.add(newArrayList)
        }

        val graphBuilder = GraphBuilder(
            GraphTypes.LINEARREGRESSIONADALINE,
            network.metricData.get("layerWeights")!!,
            inputs,
            network
        )
        graphBuilder.drawGraph()

        readlnOrNull()

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