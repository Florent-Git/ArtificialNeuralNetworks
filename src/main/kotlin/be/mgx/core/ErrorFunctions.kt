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
typealias ErrorFunction = NeuralNetwork.(
    expected: Matrix,
    output: Matrix,
    input: Matrix,
    layers: List<Layer>,
    learningRate: Float,
    batchCount: Int
) -> Unit

object ErrorFunctions {
    fun basicErrorFunction(): ErrorFunction {
        var errorCount = 0
        return { expected, output, input, layers, learningRate, _ ->
            val error = expected - output
            layers[0].weights = layers[0].weights + (learningRate * error * input).transpose()
            if (error.toScalar() != 0f) {
                errorCount++
                LOG.info("Errors: $errorCount")
            }
        }
    }
    fun simpleGradientError(batchSize: Int): ErrorFunction {
        var correctingTerms: Matrix? = null

        return { expected, output, input, layers, learningRate, batchCount ->
            if (correctingTerms == null) {
                correctingTerms = Matrix.createMatrix(layers[0].weights.cols, layers[0].weights.rows)
            }

            correctingTerms = correctingTerms!! + ((expected - output) * learningRate) * input

            if (batchCount == batchSize - 1) {
                LOG.info("Correcting terms: ${correctingTerms.toString().trim()}")
                layers[0].weights = layers[0].weights + (correctingTerms!!).transpose()

                correctingTerms = null
            }
        }
    }
}
