package be.mgx.core

import be.mgx.core.math.Matrix
import be.mgx.functions.ActivationFunction
import be.mgx.functions.ActivationFunctionSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Layer(
    var weights: Matrix,
    @Serializable(with = ActivationFunctionSerializer::class)
    val fn: ActivationFunction,
)

object Layers {
    fun createLayer(
        nbOfInput: Int,
        nbOfOutput: Int,
        fn: ActivationFunction,
        weightsInit: (Int) -> Double = { 0.0 }
    ): Layer {
        return Layer(Matrix.createMatrix(nbOfInput + 1, nbOfOutput) { size -> List(size, weightsInit) }, fn)
    }
}
