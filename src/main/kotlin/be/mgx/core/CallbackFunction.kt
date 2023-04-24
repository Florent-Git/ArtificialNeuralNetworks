package be.mgx.core

import be.mgx.core.math.Matrix
import be.mgx.core.math.times
import be.mgx.util.LOG
import kotlin.reflect.full.memberProperties

data class MetricNetworkData(
    var inputs: List<Matrix>,
    var actualInput: Matrix,
    var outputs: List<Matrix>,
    var actualOutput: Matrix,
    var expectedOutput: Matrix,
    var layers: List<Layer>,
    var batchCount: Int,
    var batchSize: Int,
    var iteration: Int
)

typealias MetricCallback = NeuralNetwork.(data: MetricNetworkData) -> Unit

private fun verifyFields(data: MetricNetworkData, vararg fields: String) {
    val anyNullField = MetricNetworkData::class.memberProperties
        .filter { fields.contains(it.name) }
        .map { Pair(it.name, it.get(data)) }
        .filter { it.second == null }

    if (anyNullField.isNotEmpty()) {
        val emptyFields = anyNullField.map { it.first }.reduce { acc, s -> "$acc, $s" }
        throw IllegalStateException("MetricNetworkData object contains empty properties that shouldn't be ($emptyFields)")
    }
}

val saveLayerWeights: MetricCallback = { data ->
    verifyFields(data, "batchCount", "inputSize", "layers")
    if (data.batchCount == data.inputs.size - 2) {
        var layerWeights = metricData.get<MutableList<Matrix>>("layerWeights")
        if (layerWeights == null) {
            layerWeights = mutableListOf()
            metricData.set("layerWeights", layerWeights)
            LOG.trace("Logging weight values after iteration ${data.iteration}")
        }
        layerWeights.add(data.layers[0].weights)
    }
}

fun meanSquareError(): MetricCallback {
    return { data ->
        verifyFields(data, "")

        var mse = metricData.get<MutableList<Double>>("mse")
        if (mse == null) {
            mse = mutableListOf()
            metricData.set("mse", mse)
        }

        if ((data.batchCount % data.batchSize) == data.batchSize - 1) {
            val tempList = mutableListOf<Double>()

            for (i in 0 until data.inputs.size) {
                val actualY = this.fire(data.inputs[i])
                val error = 0.5 * (data.outputs[i] - actualY).pow(2.0)
                tempList.add(error.sum())
            }

            val meanSquaredError = tempList.average()
            mse.add(meanSquaredError)
            LOG.info("MSE: $meanSquaredError")
        }
    }
}

val errorCount: MetricCallback = { data ->
    if (this.metricData.get<Int>("errorCount") == null) {
        this.metricData.set("errorCount", 0)
    }

    if ((data.expectedOutput - data.actualOutput).toScalar() != 0.0) {
        val errorCount = this.metricData.get<Int>("errorCount")
        this.metricData.set("errorCount", errorCount!! + 1)
    }
}
