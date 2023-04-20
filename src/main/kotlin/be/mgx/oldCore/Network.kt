package be.mgx.oldCore

import be.mgx.functions.ActivationFunction

class Network(
    private val layers: List<List<Neuron>>
) {
    fun propagate(vararg inputs: Float): List<Float> {
        if (inputs.size != layers[0].size) {
            throw InvalidInputException("Number of input doesn't match the number of input neurons")
        }

        var output = inputs.toList()

        for (layer in layers) {
            output = fireNeurons(output, layer)
        }

        return output
    }

    fun getModel(): NeuronModel {
        val weights = layers.drop(1)   // Drops the first layer which is essentially just input identity neurons
            .dropLast(1)        // Drops the last layer which is essentially just output identity neurons
            .map { layer ->
                layer.map { it.inputWeights }
            }

        val functions = layers.drop(1)
            .dropLast(1)
            .flatMap { layer -> layer.map { it.function.name } }
            .distinct()

        return NeuronModel(weights, functions)
    }

    private fun fireNeurons(
        inputs: List<Float>,
        neurons: List<Neuron>
    ): List<Float> {
        return neurons.map {
            it.fire(inputs)
        }
    }

    companion object Factory {
        @JvmStatic
        fun createNetwork(
            inputs: Int,
            outputs: Int,
            vararg layers: Int,
            layersInit: (index: Int) -> ActivationFunction
        ): Network {
            val inputNeurons = List(inputs) { InputNeuron() }
            val hiddenNeurons = mutableListOf<List<Neuron>>()

            layers.forEachIndexed { layerIndex, numberOfNeurons ->
                hiddenNeurons.add(List(numberOfNeurons) { numberOfWeights ->
                    Neuron.createRandomNeuron(numberOfWeights, layersInit(layerIndex))
                })
            }

            val outputNeurons = List(outputs) { OutputNeuron() }

            val layerList = mutableListOf<List<Neuron>>().apply {
                add(inputNeurons)
                addAll(hiddenNeurons)
                add(outputNeurons)
            }

            return Network(layerList)
        }

        @JvmStatic
        fun createFromModel(
            model: NeuronModel
        ): Network {
            val (weights, functions) = model

            val neurons = weights.zip(functions)
                .map { (weight, fn) ->
                    weight.map {
                        Neuron(it, ActivationFunction.valueOf(fn))
                    }
                }
            return Network(neurons)
        }
    }
}