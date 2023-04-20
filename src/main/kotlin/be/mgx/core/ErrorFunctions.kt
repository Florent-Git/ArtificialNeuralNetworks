package be.mgx.core

import be.mgx.core.math.Matrix
import be.mgx.core.math.times
import be.mgx.util.LOG

/**
 * @param expected The expected output
 * @param output The actual output of the network
 * @param input The inputs used in the layer
 * @param layers The different layers of the NN
 * @param learningRate The learning rate of the NN
 */
typealias ErrorFunction = (
    expected: Matrix,
    output: Matrix,
    input: Matrix,
    layers: List<Layer>,
    learningRate: Float
) -> Boolean

object ErrorFunctions {
    fun basicErrorFunction(
        expected: Matrix,
        output: Matrix,
        input: Matrix,
        layers: List<Layer>,
        learningRate: Float
    ): Boolean {
        val error = expected - output
        layers[0].weights = layers[0].weights + (learningRate * error * input).transpose()
        return error.toScalar() != 0f
    }

    fun keepErrorCount(
        errorFn: ErrorFunction
    ): ErrorFunction {
        var errorCount = 0
        return { expected, output, input, layers, learningRate ->
            val b = errorFn(expected, output, input, layers, learningRate)
            if (b) {
                errorCount++
                LOG.info("Errors: $errorCount")
            }
            b
        }
    }
}
