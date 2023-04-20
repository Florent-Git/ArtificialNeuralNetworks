package be.mgx.oldCore.io

import be.mgx.oldCore.NeuronModel
import java.io.Closeable

interface INeuronModelOutput : Closeable {
    fun writeModel(model: NeuronModel)
}