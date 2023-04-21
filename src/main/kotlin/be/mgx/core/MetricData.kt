package be.mgx.core

data class MetricData(
    val map: MutableMap<String, Any> = mutableMapOf()
) {
    fun <T> get(key: String): T? {
        return map[key] as T?
    }
}