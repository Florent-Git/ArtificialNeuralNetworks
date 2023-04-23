package be.mgx.core

import be.mgx.core.math.Matrix
import be.mgx.functions.ActivationFunction
import be.mgx.functions.ActivationFunctionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

fun main() {
    val matrix = Matrix.createMatrix(2, 3) { listOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0) }
    println(Json.encodeToString<Matrix>(matrix))

    val layers = Layer(matrix, ActivationFunction.RELU)
    println(Json.encodeToString(layers))
}
