package be.mgx.oldCore.io

import be.mgx.oldCore.NeuronModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.OutputStream

class NeuronModelOutputStream(
    val outputStream: OutputStream
): OutputStream(), INeuronModelOutput {
    override fun write(b: Int) {
        outputStream.write(b)
    }

    override fun close() {
        outputStream.close()
    }

    override fun flush() {
        outputStream.flush()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun writeModel(model: NeuronModel) {
        Json.encodeToStream(model, outputStream)
    }
}