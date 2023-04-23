package be.mgx.ui

import be.mgx.core.IAbstractNetwork
import org.beryx.textio.TextIO
import org.beryx.textio.system.SystemTextTerminal
import java.io.File

class AbstractNetworkRunner(
    private val abstractNetwork: IAbstractNetwork
): IAbstractNetwork by abstractNetwork, Runnable {
    private val textIO = TextIO(SystemTextTerminal())

    enum class AbstractNetworkCommands(
        private val label: String
    ) {
        INIT("Init"),
        TRAIN("Train"),
        EXECUTE("Execute");

        override fun toString(): String {
            return label
        }
    }

    fun init() {
        val modelFile = getFile("Model file path")
        init(modelFile)
    }

    fun train() {
        val modelFile = getFile("Model file path")
        val dataFile = getFile("Data file path")

        train(modelFile, dataFile)
    }

    fun execute() {
        val modelFile = getFile("Model file path")
        val inputs = textIO.newDoubleInputReader()
            .readList("Inputs (seperated by commas)")

        execute(modelFile, inputs)
    }

    private fun getFile(prompt: String): File {
        return File(
            textIO.newStringInputReader()
                .read(prompt)
        )
    }

    override fun run() {
        when (textIO.newEnumInputReader(AbstractNetworkCommands::class.java)
                .read("What do you want to do ?")) {
            AbstractNetworkCommands.INIT -> this.init()
            AbstractNetworkCommands.TRAIN -> this.train()
            AbstractNetworkCommands.EXECUTE -> this.execute()
            else -> throw IllegalArgumentException("Wrong argument")
        }
    }
}