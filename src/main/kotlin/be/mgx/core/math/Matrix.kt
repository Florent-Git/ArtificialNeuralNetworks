package be.mgx.core.math

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.pow

/**
 * Minimal matrix implementation for the sake of the project
 */
@Serializable
data class Matrix(
    val rows: Int,
    val cols: Int,
    private var _array: MutableList<Float> = mutableListOf()
) {
    val array: List<Float>
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

    operator fun times(scalar: Float): Matrix {
        val result = createMatrix(this.rows, this.cols)

        for (i in 0 until this.rows) {
            for (j in 0 until this.cols) {
                result[i, j] = this[i, j] * scalar
            }
        }

        return result
    }

    operator fun set(i: Int, j: Int, value: Float) {
        _array[(i * cols) + j] = value
    }

    operator fun get(i: Int, j: Int): Float {
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

    fun toScalar(): Float {
        if (!isScalar()) {
            throw IllegalStateException("The matrix ($rows x $cols) is not scalar")
        }

        return this[0, 0]
    }

    fun pow(power: Float): Matrix {
        val result = this.copy()
        result._array = _array.map { it.pow(power) }.toMutableList()
        return result
    }

    operator fun div(n: Float): Matrix {
        val result = this.copy()
        result._array = _array.map { it / n }.toMutableList()
        return result
    }

    companion object {
        fun fromScalar(scalar: Float): Matrix {
            return createMatrix(1, 1) { listOf(scalar) }
        }

        fun createMatrix(
            rows: Int,
            cols: Int,
            init: (Int) -> List<Float> = { size -> List(size) { 0f } }
        ): Matrix {
            return Matrix(
                rows,
                cols,
                init(rows * cols).toMutableList()
            )
        }
    }
}

operator fun Float.times(mat: Matrix): Matrix {
    return mat * this
}
