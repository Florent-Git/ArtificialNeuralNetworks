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
    result: NeuralNetworkResult,
    input: Matrix,
    layers: List<Layer>,
    learningRate: Double,
    batchCount: Int
) -> Unit

object ErrorFunctions {
    fun basicErrorFunction(): ErrorFunction {
        var errorCount = 0
        return { expected, result, input, layers, learningRate, _ ->
            val error = expected - result.outputs.last()
            layers[0].weights = layers[0].weights + (learningRate * error * input).transpose()
            if (error.toScalar() != 0.0) {
                errorCount++
                LOG.info("Errors: $errorCount")
            }
        }
    }

    fun simpleGradientError(batchSize: Int): ErrorFunction {
        var correctingTerms: Matrix? = null

        return { expected, result, input, layers, learningRate, batchCount ->
            if (correctingTerms == null) {
                correctingTerms = Matrix.createMatrix(layers[0].weights.cols, layers[0].weights.rows)
            }

            correctingTerms =
                correctingTerms!! + (((expected - result.outputs.last()) * learningRate).transpose() * input)

            if (((batchCount + 1) % batchSize) == 0) {
                LOG.trace("Correcting terms: ${correctingTerms.toString().trim()}")
                layers[0].weights = layers[0].weights + (correctingTerms!!).transpose()

                correctingTerms = null
            }
        }
    }

    fun gradientError(): ErrorFunction {
        return { expected, result, input, layers, learningRate, _ ->
            val deltas = getDeltas(expected, result, input, layers)

            for (item in layers.withIndex()) {
                val layer = item.value
                val i = item.index

                val tmpValue = if (i == 0) input else result.outputs[i - 1]
                layer.weights = layer.weights - learningRate * (tmpValue.transpose() * deltas[i])
            }
        }
    }

    private fun getDeltas(
        expected: Matrix,
        result: NeuralNetworkResult,
        input: Matrix,
        layers: List<Layer>
    ): List<Matrix> {
        val deltaList = MutableList<Matrix?>(layers.size) { null }

        fun _getDelta(i: Int): Matrix {
            when (i) {
                0 -> {
                    val tmpMatrix = (_getDelta(1) * layers[1].weights.transpose())
                    val delta = tmpMatrix
                        .copy(
                            rows = tmpMatrix.rows,
                            cols = tmpMatrix.cols - 1,
                            _array = tmpMatrix.array.takeLast(tmpMatrix.array.size - 1).toMutableList()
                        ).hadamard(layers[i].fn.invokeDerivative(input * layers[i].weights))
                    deltaList.add(i, delta)
                    return delta
                }
                layers.lastIndex -> {
                    val lastLayer = layers.last()
                    val lastDf = lastLayer.fn::invokeDerivative

                    val delta = (expected - result.outputs.last()).hadamard(
                        lastDf(result.outputs[i - 1] * lastLayer.weights)
                    )
                    deltaList.add(i, delta)
                    return delta
                }
                else -> {
                    val tmpMatrix = (_getDelta(i + 1) * layers[i + 1].weights.transpose())
                    val delta = tmpMatrix.copy(
                        rows = tmpMatrix.rows,
                        cols = tmpMatrix.cols - 1,
                        _array = tmpMatrix.array.takeLast(tmpMatrix.array.size - 1).toMutableList()
                    ).hadamard(
                        layers[i].fn.invokeDerivative(result.outputs[i - 1] * layers[i].weights)
                    )
                    deltaList.add(i, delta)
                    return delta
                }
            }
        }

        _getDelta(0)

        return deltaList.filterNotNull()
    }
}
