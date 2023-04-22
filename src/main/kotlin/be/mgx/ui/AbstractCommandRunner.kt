package be.mgx.ui

import org.beryx.textio.TextIO
import org.beryx.textio.system.SystemTextTerminal

abstract class AbstractCommandRunner<T, L : Runnable>(
    private val enumClass: Class<T>
): Runnable
        where T: Enum<T>, T: ICommandEnum<L> {

    private val textIO = TextIO(SystemTextTerminal())

    override fun run() {
        val lessonExample = textIO.newEnumInputReader(enumClass)
            .read("What lesson example ?")

        val commandInstance = lessonExample.commandClassProvider()
        commandInstance.run()
    }
}