package be.mgx.oldCore

import be.mgx.functions.ActivationFunction

/**
 * These neurons correspond to a kind of "identity" neurons. That means that anything entering this neuron will exit it
 * without changing the input's value.
 */
class InputNeuron: Neuron(mutableListOf(0f, 1f), ActivationFunction.LINEAR)
typealias OutputNeuron = InputNeuron
