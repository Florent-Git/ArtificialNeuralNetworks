package be.mgx.core.math

import kotlinx.serialization.Serializable

/**
 * Minimal matrix implementation for the sake of the project
 */
@Serializable
data class Matrix(
    val rows: Int,
    val cols: Int,
    private var _array: MutableList<Double> = mutableListOf()
) {
    val array: List<Double>
        get() = _array

    operator fun times(mat: Matrix): Matrix {
        if (this.cols != mat.rows && !isScalar()) {
            throw IllegalArgumentException("Cannot multiply a ${rows}x${cols} matrix with a ${mat.rows}x${mat.cols}")
        }

        if (isScalar()) {
            return mat * this[0, 0]
        } else if (mat.isScalar()) {
            return this * mat[0, 0]
        }

        val result = createMatrix(this.rows, mat.cols)

        for (i in 0 until this.rows) {
            for (j in 0 until mat.cols) {
                for (k in 0 until mat.rows) {
                    result[i, j] += this[i, k] * mat[k, j]
                }
            }
        }

        return result
    }

    operator fun plus(mat: Matrix): Matrix {
        if (mat.cols != this.cols || mat.rows != this.rows) {
            throw IllegalArgumentException("Cannot add a ${rows}x${cols} matrix with a ${mat.rows}x${mat.cols}")
        }

        val result = createMatrix(this.rows, this.cols)

        for (i in 0 until this.rows) {
            for (j in 0 until this.cols) {
                result[i, j] = this[i, j] + mat[i, j]
            }
        }

        return result
    }

    operator fun times(scalar: Double): Matrix {
        val result = createMatrix(this.rows, this.cols)

        for (i in 0 until this.rows) {
            for (j in 0 until this.cols) {
                result[i, j] = this[i, j] * scalar
            }
        }

        return result
    }

    operator fun set(i: Int, j: Int, value: Double) {
        _array[(i * cols) + j] = value
    }

    operator fun get(i: Int, j: Int): Double {
        return _array[(i * cols) + j]
    }

    override fun toString(): String {
        val str = StringBuilder()
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                str.append(this[i, j])
                str.append('\t')
            }
            str.append('\n')
        }
        return str.toString()
    }

    operator fun unaryMinus(): Matrix {
        val result = createMatrix(rows, cols)

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[i, j] = -this[i, j]
            }
        }

        return result
    }

    operator fun minus(output: Matrix): Matrix {
        return plus(-output)
    }

    fun isScalar(): Boolean {
        return cols == 1 && cols == rows
    }

    fun transpose(): Matrix {
        val result = createMatrix(cols, rows)

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[j, i] = this[i, j]
            }
        }

        return result
    }

    fun toScalar(): Double {
        if (!isScalar()) {
            throw IllegalStateException("The matrix ($rows x $cols) is not scalar")
        }

        return this[0, 0]
    }

    operator fun div(n: Double): Matrix {
        val result = this.copy()
        result._array = _array.map { it / n }.toMutableList()
        return result
    }

    companion object {

        fun createMatrix(
            rows: Int,
            cols: Int,
            init: (Int) -> List<Double> = { size -> List(size) { 0.0 } }
        ): Matrix {
            return Matrix(
                rows,
                cols,
                init(rows * cols).toMutableList()
            )
        }
    }
}

operator fun Double.times(mat: Matrix): Matrix {
    return mat * this
}
