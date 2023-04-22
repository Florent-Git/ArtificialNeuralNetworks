package be.mgx.networks

import be.mgx.core.*
import be.mgx.core.math.Matrix
import be.mgx.functions.ActivationFunction
import be.mgx.util.GraphBuilder
import be.mgx.util.GraphTypes
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import picocli.CommandLine.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalSerializationApi::class)
@Command(name = "logical-and", abbreviateSynopsis = true)
class BasicAndPerceptron : IAbstractNetwork {
    @Command
    override fun init(
        @Option(names = ["-m", "--model"])
        model: File,
    ): Int {
        val network = NeuralNetwork.createNetwork(
            Layers.createLayer(2, 1, ActivationFunction.RELU)
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
        dataFile: File,
        @Option(names = ["-I", "--iterations"], description = ["Number of iterations"], defaultValue = "10")
        iterations: Int
    ): Int {
        val network = retrieveNetworkModelFromFile(modelFile)
        val model = retrieveDataFromCsv(dataFile)

        var (X, Y) = model.map { it.chunked(2) }
            .transpose()

        X = X.map { x -> listOf(1f) + x } // Prepend 1f to make the matrix a 1x3 input matrix

        network.train(
            X.map { x -> Matrix.createMatrix(1, 3) { x } },
            Y.map { y -> Matrix.createMatrix(1, 1) { y } },
            1f,
            ErrorFunctions.basicErrorFunction(),
            StopFunctions.iterationStopFunction(iterations),
            listOf(saveLayerWeights)
        )

        saveNetworkModelToFile(network, modelFile)

        val inputs = mutableListOf<ArrayList<Float>>()

        for (arrayList in model) {
            val newArrayList = arrayListOf(arrayList[0], arrayList[1])
            inputs.add(newArrayList)
        }

        val graphBuilder = GraphBuilder(GraphTypes.BASICANDPERCEPTRON,
            network.metricData.get("layerWeights")!!,
            inputs)
        graphBuilder.drawGraph()

        readlnOrNull()

        return 0
    }

    @Command
    override fun execute(
        @Option(names = ["-m", "--model"])
        model: File,
        @Parameters(description = ["Inputs (separated by commas) for the neural network"], split = ",")
        inputs: List<Float>
    ): Int {
        val network = retrieveNetworkModelFromFile(model)
        val input = Matrix.createMatrix(1, inputs.size) { inputs }

        val output = network.fire(input)

        println("Output: $output")

        return 0
    }

    private fun retrieveNetworkModelFromFile(model: File): NeuralNetwork {
        return FileInputStream(model).use { stream ->
            Json.decodeFromStream<NeuralNetwork>(stream)
        }
    }

    private fun saveNetworkModelToFile(network: NeuralNetwork, model: File) {
        FileOutputStream(model).use { stream ->
            Json.encodeToStream<NeuralNetwork>(network, stream)
        }
    }

    private fun retrieveDataFromCsv(dataFile: File): List<List<Float>> {
        val dataStream = FileInputStream(dataFile)
        val dataString = String(dataStream.readAllBytes(), StandardCharsets.UTF_8)

        val csvReader = Csv {}
        return csvReader.decodeFromString<List<List<Float>>>(dataString)
    }
}