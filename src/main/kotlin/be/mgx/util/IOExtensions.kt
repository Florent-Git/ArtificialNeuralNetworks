package be.mgx.util

import be.mgx.core.NeuralNetwork
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

fun retrieveNetworkModelFromFile(model: File): NeuralNetwork {
    return FileInputStream(model).use { stream ->
        Json.decodeFromStream<NeuralNetwork>(stream)
    }
}

fun saveNetworkModelToFile(network: NeuralNetwork, model: File) {
    FileOutputStream(model).use { stream ->
        Json.encodeToStream<NeuralNetwork>(network, stream)
    }
}

fun retrieveDataFromCsv(dataFile: File): List<List<Float>> {
    val dataStream = FileInputStream(dataFile)
    val dataString = String(dataStream.readAllBytes(), StandardCharsets.UTF_8)

    val csvReader = Csv {}
    return csvReader.decodeFromString<List<List<Float>>>(dataString)
}