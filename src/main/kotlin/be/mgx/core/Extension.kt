package be.mgx.core


fun <E> List<List<E>>.transpose(): List<List<E>> {
    val newArray = MutableList(this[0].size) { MutableList<E?>(this.size) { null } }

    for (i in 0 until this.size) {
        for (j in 0 until this[i].size) {
            newArray[j][i] = this[i][j]
        }
    }

    return newArray as List<List<E>>
}
