package be.mgx.oldCore.io

import be.mgx.oldCore.BasicData
import java.io.Closeable

interface INeuronDataInput<T: BasicData>: Closeable {
    fun readData(): T
}