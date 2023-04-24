package be.mgx.util

import be.mgx.core.math.Matrix

fun Matrix.addOne(): Matrix {
    return this.copy(
        this.rows,
        this.cols + 1,
        (listOf(1.0) + this.array).toMutableList()
    )
}
