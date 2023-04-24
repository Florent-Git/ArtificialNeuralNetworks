package be.mgx.networks.multilayer

import be.mgx.core.*
import be.mgx.core.math.Matrix
import be.mgx.functions.ActivationFunction
import be.mgx.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import picocli.CommandLine
import java.io.File
import java.io.FileOutputStream
import java.util.Random


@OptIn(ExperimentalSerializationApi::class)
@CommandLine.Command(name = "multi-nonlinear-regression", abbreviateSynopsis = true)
class NonLinearRegression: IAbstractNetwork {
    @CommandLine.Command
    override fun init(
        @CommandLine.Option(names = ["-m", "--model"])
        model: File,
    ): Int {
        val random = Random() // 1234 is for testing purposes
        val network = NeuralNetwork.createNetwork(
            Layers.createLayer(1, 10, ActivationFunction.SIGMOID) { random.nextGaussian() },
            Layers.createLayer(10, 1, ActivationFunction.SIGMOID) { random.nextGaussian() }
        )

        val fileStream = FileOutputStream(model)
        Json.encodeToStream<NeuralNetwork>(network, fileStream)

        return 0
    }

    @CommandLine.Command
    override fun train(
        @CommandLine.Option(names = ["-m", "--model"])
        modelFile: File,
        @CommandLine.Option(names = ["-d", "--data"], description = ["Input file containing the training data"])
        dataFile: File
    ): Int {
        val network = retrieveNetworkModelFromFile(modelFile)
        val model = retrieveDataFromCsv(dataFile)

        var (X, Y) = model.map { it.chunked(1) }
            .transpose()

        X = X.map { x -> listOf(1.0) + x }
        network.train(
            X.map { x -> Matrix.createMatrix(1, 2) { x } },
            Y.map { y -> Matrix.createMatrix(1, 1) { y } },
            0.8,
            ErrorFunctions.gradientError(),
            StopFunctions.iterationStopFunction(2000),
            listOf(saveLayerWeights, meanSquareError()),
            X.size
        )

        saveNetworkModelToFile(network, modelFile)

        val inputs = mutableListOf<ArrayList<Double>>()
        for (arrayList in model) {
            val classValue = 1.0
            val index = arrayList.lastIndexOf(classValue).toDouble()
            val newArrayList = arrayListOf(arrayList[0], arrayList[1], index)
            inputs.add(newArrayList)
        }

        val graphBuilder = GraphBuilder(
            GraphTypes.NONLINEARREGRESSION,
            network.metricData.get("layerWeights")!!,
            inputs,
            network)
        graphBuilder.drawGraph()

        graphBuilder.isWindowClosed.join()

        return 0
    }

    @CommandLine.Command
    override fun execute(
        @CommandLine.Option(names = ["-m", "--model"])
        model: File,
        @CommandLine.Parameters(description = ["Inputs (separated by commas) for the neural network"], split = ",")
        inputs: List<Double>
    ): Int {
        TODO("Not yet implemented")
    }
}