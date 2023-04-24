package be.mgx.core

import be.mgx.core.math.Matrix

data class NeuralNetworkResult(
    val potentials: List<Matrix>,
    val outputs: List<Matrix>
)
