package be.mgx.core

import be.mgx.util.LOG

typealias StopFunction = () -> Boolean

object StopFunctions {
    fun iterationStopFunction(nbIterations: Int): StopFunction {
        var iteration = 0
        return {
            LOG.info("Done iteration ${iteration + 1}")
            iteration++ == nbIterations - 1
        }
    }
}
