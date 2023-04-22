package be.mgx.core

data class MetricData(
    val map: MutableMap<String, Any> = mutableMapOf()
) {
    fun <T> get(key: String): T? {
        return map[key] as T?
    }

    fun <T : Any> set(key: String, value: T) {
        map[key] = value
    }

}