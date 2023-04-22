package be.mgx.ui

interface ICommandEnum<T : Any> {
    val label: String
    val commandClassProvider: () -> T
}