package be.mgx.oldCore.io

import be.mgx.oldCore.NeuronModel
import java.io.Closeable

interface INeuronModelInput : Closeable {
    fun readModel(): NeuronModel
}