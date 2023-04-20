package be.mgx.core

import picocli.CommandLine.Command
import java.io.File

abstract class AbstractNetwork {
    /**
     * Initialises the model
     */
    abstract fun init(
        model: File
    ): Int

    /**
     * Trains the model
     */
    abstract fun train(
        modelFile: File,
        dataFile: File,
        iterations: Int
    ): Int

    /**
     * Executes the model
     */
    abstract fun execute(
        model: File,
        inputs: List<Float>
    ): Int
}
