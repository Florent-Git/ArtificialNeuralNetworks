package be.mgx.core

import java.io.File

interface IAbstractNetwork {
    /**
     * Initialises the model
     */
    fun init(
        model: File
    ): Int

    /**
     * Trains the model
     */
    fun train(
        modelFile: File,
        dataFile: File
    ): Int

    /**
     * Executes the model
     */
    fun execute(
        model: File,
        inputs: List<Double>
    ): Int
}
