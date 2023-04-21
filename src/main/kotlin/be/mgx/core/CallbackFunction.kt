package be.mgx.core

import be.mgx.core.math.Matrix


typealias MetricCallback = MetricData.(
    input: Matrix,
    output: Matrix,
    expectedOutput: Matrix,
    layers: List<Layer>
) -> Unit

val countErrors: MetricCallback = { input, output, expectedOutput, layers ->
    val error = output - expectedOutput
    if (error.toScalar() != 0f) {
        // ((Int) this.map["nbErrors"]) += 1
    }
}

val weightsHistory: MetricCallback = { input, output, expectedOutput, layers ->
    // this.map["weights"] += layers.map { it.weights }
}
