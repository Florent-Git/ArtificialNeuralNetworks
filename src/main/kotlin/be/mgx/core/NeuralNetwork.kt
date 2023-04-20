package be.mgx.core

import be.mgx.core.math.Matrix
import be.mgx.core.math.times
import be.mgx.functions.ActivationFunction
import be.mgx.util.LOG
import kotlinx.serialization.Serializable

@Serializable
class NeuralNetwork private constructor(private val layers: List<Layer>) {
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
        stopConditionFunction: StopFunction
    ) {
        var iteration = 0
        do {
            LOG.info("Starting iteration $iteration -----------------------------")
            val inputOutput = inputs.zip(expectedOutputs)

            for ((input, expectedOutput) in inputOutput) {
                val output = fire(input)
                LOG.info("For input ${input.toString().trim()}, outputted ${output.toString().trim()} (expected ${expectedOutput.toString().trim()})")
                errorFunction(expectedOutput, output, input, layers, learningRate)
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
