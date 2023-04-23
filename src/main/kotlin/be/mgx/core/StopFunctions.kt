package be.mgx.core

import be.mgx.util.LOG
import java.util.function.Predicate

typealias StopFunction = NeuralNetwork.(

) -> Boolean

object StopFunctions {
    fun iterationStopFunction(nbIterations: Int): StopFunction {
        var iteration = 0
        return {
            LOG.info("Done iteration ${iteration + 1}")
            iteration++ == nbIterations - 1
        }
    }

    fun untilErrorCountIs(predicate: Predicate<Int>): StopFunction {
        return {
            val errorCount = metricData.get<Int>("errorCount")
                    ?: throw IllegalStateException("errorCount metric is not set")
            metricData.set("errorCount", 0)
            predicate.test(errorCount)
        }
    }
}
