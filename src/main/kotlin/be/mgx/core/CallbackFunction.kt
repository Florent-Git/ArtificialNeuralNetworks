package be.mgx.core

import be.mgx.core.math.Matrix
import be.mgx.core.math.times
import kotlin.time.times


typealias MetricCallback = MetricData.(
    input: Matrix,
    output: Matrix,
    expectedOutput: Matrix,
    layers: List<Layer>,
    batchCount: Int
) -> Unit

val countErrors: MetricCallback = { input, output, expectedOutput, layers, _ ->
    val error = output - expectedOutput
    if (error.toScalar() != 0f) {
        // ((Int) this.map["nbErrors"]) += 1
    }
}

val weightsHistory: MetricCallback = { input, output, expectedOutput, layers, _ ->
    // this.map["weights"] += layers.map { it.weights }
}

val computeMSE: MetricCallback = { input, output, expectedOutput, _, batchCount ->
//    val mse = (2f / expectedOutput.array.size) * input.transpose() * (expectedOutput - output)
//    var mseList = get<MutableList<Matrix>>("mse")
//    if (mseList == null) {
//        mseList = mutableListOf()
//        set("mse", mseList)
//    }
//    mseList.add(mse)
}
