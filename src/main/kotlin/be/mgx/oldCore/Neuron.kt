package be.mgx.oldCore

import be.mgx.functions.ActivationFunction
import java.util.*

open class Neuron(
    val inputWeights: MutableList<Float>,
    val function: ActivationFunction
) {
    fun fire(inputs: List<Float>): Float {
        if (inputWeights.size != inputs.size + 1) {
            throw IncorrectModelException("The neural model is incorrect")
        }

        val augmentedInputs = listOf(1f) + inputs;
        val result = augmentedInputs
            .zip(inputWeights)
            .map { (i, w) -> i * w }
            .sum()

        return 0f //function(result)
    }

    companion object Factory {
        fun createRandomNeuron(
            nOfWeights: Int,
            function: ActivationFunction
        ): Neuron {
            val random = Random()
            return createNeuron(nOfWeights, function) {
                random.nextGaussian().toFloat()
            }
        }

        fun createNeuron(
            nOfWeights: Int,
            function: ActivationFunction,
            init: () -> Float
        ): Neuron {
            val weights =  (0..nOfWeights).map {
                init()
            }.toMutableList()
            return Neuron(weights, function)
        }
    }
}