package be.mgx.core

import be.mgx.core.math.Matrix
import be.mgx.util.LOG
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class NeuralNetwork private constructor(private val layers: List<Layer>) {
    @Transient
    val metricData: MetricData = MetricData()

    fun fire(input: Matrix): Matrix {
        var currentLayer: Matrix = input

        for (layer in layers) {
            currentLayer = layer.fn(currentLayer * layer.weights)
        }

        return currentLayer
    }

    fun train(
        inputs: List<Matrix>,
        expectedOutputs: List<Matrix>,
        learningRate: Float,
        errorFunction: ErrorFunction,
        stopConditionFunction: StopFunction,
        metricCallbackFunctions: List<MetricCallback> = listOf(),
        batchSize: Int = 1
    ) {
        var iteration = 0
        do {
            LOG.info("Starting iteration $iteration -----------------------------")
            val inputOutput = inputs.zip(expectedOutputs)

            var batchCount = 0

            for ((input, expectedOutput) in inputOutput) {
                val output = fire(input)
                LOG.info("For input ${input.toString().trim()}, outputted ${output.toString().trim()} (expected ${expectedOutput.toString().trim()})")

                metricCallbackFunctions.forEach { fn -> metricData.fn(input, output, expectedOutput, layers, batchCount) }
                //if (batchCount == batchSize - 1) {
                    this.errorFunction(expectedOutput, output, input, layers, learningRate, batchCount++)
                //}
            }
            iteration++
        } while (!stopConditionFunction())
    }

    companion object Factory {
        @JvmStatic
        fun createNetwork(
            vararg layers: Layer
        ): NeuralNetwork {
            return NeuralNetwork(layers.toList())
        }
    }
}
