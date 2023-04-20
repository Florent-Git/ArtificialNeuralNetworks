package be.mgx.oldCore.io

import be.mgx.oldCore.BasicData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString
import java.io.InputStream

class NeuronDataInputStream<T: BasicData>(
    private val inputStream: InputStream
) : InputStream(), INeuronDataInput<T> {
    @OptIn(ExperimentalSerializationApi::class)
    private val csv = Csv { }

    override fun readData(): T {
        TODO("Implementation is WIP, use readDataTEMP instead")
    }

    override fun read(): Int = inputStream.read();
    override fun close() = inputStream.close()
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified R: BasicData> NeuronDataInputStream<R>.readDataTEMP(inputStream: InputStream): List<R> {
    val csv = Csv {  }

    val string = inputStream.bufferedReader()
        .lines()
        .reduce { t, u -> "$t\n$u" }
        .get()

    return csv.decodeFromString(string)
}
