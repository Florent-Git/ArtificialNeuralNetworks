package be.mgx.neurons

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SimpleRELUNeuronTest {
    @ParameterizedTest(name = "Given input {0} and weights {1}, when applied to the neuron, should return {2}")
    @MethodSource("getTestData")
    fun `Neuron is valid`(input: List<Float>, weights: MutableList<Float>, expected: Float) {
        val neuron = SimpleRELUNeuron(weights)

        val result = neuron.fire(input)

        Assertions.assertEquals(expected, result)
    }

    companion object {
        @JvmStatic
        fun getTestData(): Stream<Arguments> {
            return listOf(
                Arguments.of(listOf(1f, 1f), listOf(1f, 1f, 1f), 1f),
                Arguments.of(listOf(1f, 1f), listOf(-1f, -1f, -1f), 0f),
                Arguments.of(listOf(0f, 0f), listOf(1f, -1f, -1f), 1f),
                Arguments.of(listOf(0f, 0f), listOf(-1f, -1f, -1f), 0f),
                Arguments.of(listOf(1f, 0f), listOf(1f, 1f, -1f), 1f),
                Arguments.of(listOf(1f, 1f), listOf(.5f, -.8f, 1f), 1f),
            ).stream()
        }
    }
}