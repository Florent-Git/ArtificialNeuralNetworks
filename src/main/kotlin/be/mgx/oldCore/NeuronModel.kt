package be.mgx.oldCore

import kotlinx.serialization.Serializable

@Serializable
data class NeuronModel(
    override val weights: List<List<MutableList<Float>>>,
    override val functions: List<String>
): INeuronModel