package be.mgx.oldCore.io

import be.mgx.oldCore.NeuronModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream

class NeuronModelInputStream(
    val inputStream: InputStream
) : InputStream(), INeuronModelInput {
    override fun read(): Int = inputStream.read()

    override fun close() = inputStream.close()

    @OptIn(ExperimentalSerializationApi::class)
    override fun readModel(): NeuronModel {
        return Json.decodeFromStream(inputStream)
    }
}
