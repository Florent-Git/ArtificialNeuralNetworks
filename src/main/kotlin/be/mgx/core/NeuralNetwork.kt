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
        val output = _fire(input).outputs.last()
        LOG.trace(
            "For input {}, outputted {}",
            input.toString().trim(),
            output.toString().trim()
        )
        return output
    }

    private fun _fire(input: Matrix): NeuralNetworkResult {
        val potentials = mutableListOf<Matrix>()
        val outputs = mutableListOf<Matrix>()

        var currentLayer: Matrix = input

        val layerIterator = layers.iterator()

        for (layer in layerIterator) {
            val potential = currentLayer * layer.weights
            potentials.add(potential)

            currentLayer = if (layerIterator.hasNext()) {
                val output = Matrix.createMatrix(potential.rows, potential.cols + 1) {
                    listOf(1.0) + layer.fn(potential).array
                }
                outputs.add(output)
                output
            } else {
                val output = layer.fn(currentLayer * layer.weights)
                outputs.add(output)
                output
            }
        }
        return NeuralNetworkResult(potentials, outputs)
    }

    fun train(
        inputs: List<Matrix>,
        expectedOutputs: List<Matrix>,
        learningRate: Double,
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
                val result = _fire(input)

                this.errorFunction(expectedOutput, result, input, layers, learningRate, batchCount)
                metricCallbackFunctions.forEach { fn -> this.fn(
                    MetricNetworkData(
                        inputs,
                        input,
                        expectedOutputs,
                        result.outputs.last(),
                        expectedOutput,
                        layers,
                        batchCount,
                        batchSize,
                        iteration
                    )
                ) }

                batchCount++
            }

            iteration++
        } while (!this.stopConditionFunction(iteration))
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
