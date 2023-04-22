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