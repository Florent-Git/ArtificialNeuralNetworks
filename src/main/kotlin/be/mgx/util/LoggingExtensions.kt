package be.mgx.util

import mu.KLogger
import mu.KotlinLogging

val Any.LOG: KLogger
    get() = KotlinLogging.logger(this::class.qualifiedName!!)

