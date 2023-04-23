package be.mgx.core

import be.mgx.core.math.Matrix


typealias MetricCallback = MetricData.(
    input: Matrix,
    output: Matrix,
    expectedOutput: Matrix,
    layers: List<Layer>,
    batchCount: Int,
    inputSize: Int
) -> Unit

val saveLayerWeights: MetricCallback = { _, _, _, layers, batchCount, inputSize ->
    if (batchCount == inputSize - 1) {
        var layerWeights = get<MutableList<Matrix>>("layerWeights")
        if (layerWeights == null) {
            layerWeights = mutableListOf()
            set("layerWeights", layerWeights)
        }
        layerWeights.add(layers[0].weights)
    }
}

fun countErrors(bias: Int): MetricCallback {
    return { input, output, expectedOutput, layers, batchCount, inputSize ->
        var errorCount = this.get<Int>("errorCount")
        if (errorCount == null) errorCount = 0

        val error = expectedOutput - (output.applyOnEach { if (it >= bias) 1.0 else -1.0 })
        if (!error.isNullMatrix()) errorCount++
        this.set("errorCount", errorCount)
    }
}
