package be.mgx.util

import java.util.Random


fun generateRandomWeights(n: Int): MutableList<Float> {
    val rnd = Random()
    return (0..n).map {
        rnd.nextGaussian().toFloat()
    }.toMutableList()
}
