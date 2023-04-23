package be.mgx.functions

import be.mgx.core.math.Matrix
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class ActivationFunction(
    private val fn: (Double) -> Double
): AbstractFunction {
    RELU({ if (it >= 0) 1.0 else 0.0 }),
    LINEAR({ it });

    override operator fun invoke(input: Matrix): Matrix {
        val result = Matrix.createMatrix(input.rows, input.cols)

        for (i in 0 until result.rows) {
            for (j in 0 until result.cols) {
                result[i, j] = fn(input[i, j])
            }
        }

        return result
    }
}

object ActivationFunctionSerializer: KSerializer<ActivationFunction> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("ActivationFunction", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ActivationFunction {
        val stringValue = decoder.decodeString()
        return ActivationFunction.valueOf(stringValue)
    }

    override fun serialize(encoder: Encoder, value: ActivationFunction) {
        encoder.encodeString(value.name)
    }

}
