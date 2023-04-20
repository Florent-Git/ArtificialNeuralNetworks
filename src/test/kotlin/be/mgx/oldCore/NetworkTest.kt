package be.mgx.oldCore

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class NetworkTest {
    class TestingNeuron(
        override val inputWeights: MutableList<Float>,
        override val function: AbstractFunction
    ) : Neuron()

    @ParameterizedTest
    @MethodSource
    fun TestIdentityNetwork(input: Float) {
        val network = Network.createNetwork(
            1,
            1,
            1,
        ) { _ -> apply(1f, 0f)() }

        val output = network.propagate(input)

        assertEquals(input, output[0])
    }

    @ParameterizedTest
    @MethodSource
    fun TestSimpleNetwork(input: Float, expected: Float) {
        val network = Network.createNetwork(
            1,
            1,
            1
        ) { _ -> apply(10f, -5f)() }

        val output = network.propagate(input)

        assertEquals(expected, output[0])
    }

    private fun apply(multiplication: Float, addition: Float): () -> TestingNeuron {
        return {
            TestingNeuron(mutableListOf(0f, multiplication)) { it + addition }
        }
    }

    companion object {
        @JvmStatic
        fun TestIdentityNetwork(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(1f)
            )
        }

        @JvmStatic
        fun TestSimpleNetwork(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(1f, 5f),
                Arguments.of(10f, 95f),
                Arguments.of(-4f, -45f),
                Arguments.of(0.5f, 0f),
            )
        }
    }
}