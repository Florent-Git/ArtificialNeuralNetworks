package be.mgx.core

import be.mgx.util.LOG
import java.util.function.Predicate

typealias StopFunction = NeuralNetwork.(
    iteration: Int
) -> Boolean

object StopFunctions {
    fun iterationStopFunction(nbIterations: Int): StopFunction {
        var iteration = 0
        return {
            LOG.info("Done iteration ${iteration + 1}")
            iteration++ == nbIterations - 1
        }
    }

    fun stopWhenMseIs(maxIterations: Int, predicate: Predicate<Double>): StopFunction {
        return {
            val mse = this.metricData.get<List<Double>>("mse")
                ?: throw IllegalStateException("Missing MSE metric")
            predicate.test(mse.last()) || it > maxIterations
        }
    }

    fun stopWhenErrorCountIs(predicate: Predicate<Int>): StopFunction {
        return {
            val errorCount = this.metricData.get<Int>("errorCount")
                ?: throw IllegalStateException("Missing errorCount metric")
            this.metricData.set("errorCount", 0)
            predicate.test(errorCount)
        }
    }
}
