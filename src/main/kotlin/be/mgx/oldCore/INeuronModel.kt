package be.mgx.oldCore

interface INeuronModel {
    val weights: List<List<MutableList<Float>>>
    val functions: List<String>
}